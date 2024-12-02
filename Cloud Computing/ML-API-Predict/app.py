import os
from flask import Flask, jsonify, request
import tensorflow as tf
import numpy as np
import json
import joblib  # For loading the numeric preprocessor
import pandas as pd  # For DataFrame operations

app = Flask(__name__)

# Define paths to assets
ASSETS_DIR = os.path.join(os.path.dirname(__file__), 'assets')
MODEL_PATH = os.path.join(ASSETS_DIR, 'mbahlaptop.tflite')
NUMERIC_PREPROCESSOR_PATH = os.path.join(ASSETS_DIR, 'numeric_preprocessor.pkl')
SUPPORTING_FILES = {
    'brand_vocabulary': os.path.join(ASSETS_DIR, 'brand_vocabulary.json'),
    'processor_vocabulary': os.path.join(ASSETS_DIR, 'processor_vocabulary.json'),
    'os_vocabulary': os.path.join(ASSETS_DIR, 'OS_vocabulary.json'),
    'storage_type_vocabulary': os.path.join(ASSETS_DIR, 'Storage_type_vocabulary.json'),
    'gpu_vocabulary': os.path.join(ASSETS_DIR, 'GPU_vocabulary.json'),
}

# Load model
print("Loading model...")
interpreter = tf.lite.Interpreter(model_path=MODEL_PATH)
interpreter.allocate_tensors()

# Load numeric preprocessor
print("Loading numeric preprocessor...")
numeric_preprocessor = joblib.load(NUMERIC_PREPROCESSOR_PATH)
print("Feature names used during training:", numeric_preprocessor.feature_names_in_)

# Load supporting files
print("Loading supporting files...")
supporting_data = {}
for key, filepath in SUPPORTING_FILES.items():
    with open(filepath, 'r') as f:
        supporting_data[key] = json.load(f)

# Access vocabularies
brand_vocab = supporting_data['brand_vocabulary']
processor_vocab = supporting_data['processor_vocabulary']
os_vocab = supporting_data['os_vocabulary']
storage_type_vocab = supporting_data['storage_type_vocabulary']
gpu_vocab = supporting_data['gpu_vocabulary']

# Helper function to encode categorical features
def encode_categorical(value, vocab):
    return vocab.get(value.lower(), 0)  # Default to [UNK] if not found

# Preprocess numerical features
def preprocess_numerical_features(data):
    # Extract numerical features into a dictionary with matching capitalization
    numerical_features = {
        "Ram": data.get("ram", 0),
        "Storage": data.get("storage", 0),
        "display_size": data.get("display_size", 0),
        "resolution_width": data.get("resolution_width", 0),
        "resolution_height": data.get("resolution_height", 0),
    }

    # Convert to a pandas DataFrame with proper column names
    numerical_df = pd.DataFrame([numerical_features])

    # Transform using the numeric preprocessor
    transformed_numerical = numeric_preprocessor.transform(numerical_df)
    return transformed_numerical

# Prediction function
def model_prediction(data):
    try:
        # Encode categorical features
        brand = encode_categorical(data.get("brand", ""), brand_vocab)
        processor = encode_categorical(data.get("processor", ""), processor_vocab)
        storage_type = encode_categorical(data.get("storage_type", ""), storage_type_vocab)
        gpu = encode_categorical(data.get("gpu", ""), gpu_vocab)
        os_value = encode_categorical(data.get("os", ""), os_vocab)

        # Preprocess numerical features
        numerical_features = preprocess_numerical_features(data)

        # Combine categorical and numerical features
        input_data = np.concatenate((
            np.array([brand, processor, storage_type, gpu, os_value], dtype=np.float32),
            numerical_features.flatten()
        ), axis=0)

        # Pad input to match model requirements
        input_details = interpreter.get_input_details()
        required_features = input_details[0]['shape'][1]
        padded_input = np.zeros((1, required_features), dtype=np.float32)
        padded_input[0, :input_data.shape[0]] = input_data

        # Perform inference
        interpreter.set_tensor(input_details[0]['index'], padded_input)
        interpreter.invoke()

        # Get prediction result
        output_details = interpreter.get_output_details()
        prediction = interpreter.get_tensor(output_details[0]['index'])[0][0]
        return float(prediction)
    except Exception as e:
        raise ValueError(f"Error during prediction: {str(e)}")

# Index route
@app.route("/")
def index():
    return jsonify({
        "status": {
            "code": 200,
            "message": "API is up and running!"
        },
        "data": None
    }), 200

# Prediction route
@app.route("/predict", methods=["POST"])
def predict():
    if request.method == "POST":
        json_data = request.get_json()

        try:
            # Validate input
            required_keys = ["brand", "processor", "ram", "ram_type", "storage", "storage_type", "gpu",
                             "display_size", "resolution_width", "resolution_height", "os"]
            missing_keys = [key for key in required_keys if key not in json_data]
            if missing_keys:
                return jsonify({
                    "status": {
                        "code": 400,
                        "message": f"Missing required fields: {', '.join(missing_keys)}"
                    },
                    "data": None
                }), 400

            # Perform prediction
            predicted_price = model_prediction(json_data)
            return jsonify({
                "status": {
                    "code": 200,
                    "message": "Prediction successful"
                },
                "data": {
                    "predicted_price": predicted_price
                }
            }), 200
        except Exception as e:
            return jsonify({
                "status": {
                    "code": 500,
                    "message": str(e)
                },
                "data": None
            }), 500
    else:
        return jsonify({
            "status": {
                "code": 405,
                "message": "Method Not Allowed"
            },
            "data": None
        }), 405

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))