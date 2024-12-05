import os
from flask import Flask, jsonify, request
import pandas as pd
import numpy as np
import tensorflow as tf
import joblib
import json

app = Flask(__name__)

# Paths to assets
ASSETS_DIR = os.path.join(os.path.dirname(__file__), 'assets')
MODEL_PATH = os.path.join(ASSETS_DIR, 'mbahlaptop_quantile_V0.1.tflite')
NUMERIC_PREPROCESSOR_PATH = os.path.join(ASSETS_DIR, 'numeric_preprocessor.pkl')
CATEGORICAL_FEATURES = ['processor', 'brand', 'storage_type', 'gpu', 'os']
NUMERIC_FEATURES = ['Ram', 'Storage', 'display_size', 'resolution_width', 'resolution_height']
REQUIRED_KEYS = NUMERIC_FEATURES + CATEGORICAL_FEATURES

# Load necessary assets at startup
print("Loading assets...")
numeric_preprocessor = joblib.load(NUMERIC_PREPROCESSOR_PATH)
categorical_vocabularies = {
    feature: json.load(open(os.path.join(ASSETS_DIR, f"{feature}_vocabulary.json"), 'r'))
    for feature in CATEGORICAL_FEATURES
}

# Load the TFLite model
print("Loading TFLite model...")
interpreter = tf.lite.Interpreter(model_path=MODEL_PATH)
interpreter.allocate_tensors()
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()


def encode_categorical(value, vocabulary):
    """Encodes a categorical feature into a one-hot vector."""
    encoded = np.zeros(len(vocabulary))
    components = value.lower().split()
    for component in components:
        if component in vocabulary:
            encoded[vocabulary.index(component)] = 1
    return encoded

def preprocess_features(data):
    """Prepares features for the model prediction."""
    # Process numerical features
    X_numeric = numeric_preprocessor.transform(
        pd.DataFrame([{key: data.get(key, 0) for key in NUMERIC_FEATURES}])
    )

    # Process categorical features
    X_categorical = np.hstack([
        encode_categorical(data.get(feature, ""), categorical_vocabularies[feature]).reshape(1, -1)
        for feature in CATEGORICAL_FEATURES
    ])

    # Combine numeric and categorical features
    return np.hstack([X_numeric, X_categorical]).astype(np.float32)

def model_prediction(data):
    """Runs the integrated ML prediction using the TFLite model."""
    try:
        X_processed = preprocess_features(data)
        interpreter.set_tensor(input_details[0]['index'], X_processed)
        interpreter.invoke()
        y_pred = interpreter.get_tensor(output_details[0]['index'])

        # Ensure proper output format
        if y_pred.ndim == 1:
            return {f"Quantile {q}": float(y_pred[i]) for i, q in enumerate([0.25, 0.5, 0.75])}
        elif y_pred.ndim == 2 and y_pred.shape[1] == 3:
            return {f"Quantile {q}": float(y_pred[0, i]) for i, q in enumerate([0.25, 0.5, 0.75])}
        else:
            raise ValueError(f"Unexpected output shape: {y_pred.shape}")
    except Exception as e:
        raise ValueError(f"Error during prediction: {e}")

@app.route("/")
def index():
    return jsonify({
        "status": {"code": 200, "message": "API is up and running!"},
        "data": None
    })

@app.route("/predict", methods=["POST"])
def predict():
    if request.method != "POST":
        return jsonify({"status": {"code": 405, "message": "Method Not Allowed"}, "data": None}), 405

    json_data = request.get_json()
    missing_keys = [key for key in REQUIRED_KEYS if key not in json_data]

    if missing_keys:
        return jsonify({
            "status": {"code": 400, "message": f"Missing required fields: {', '.join(missing_keys)}"},
            "data": None
        }), 400

    try:
        intervals = model_prediction(json_data)
        return jsonify({
            "status": {"code": 200, "message": "Prediction successful"},
            "data": {"predicted_intervals": intervals}
        }), 200
    except Exception as e:
        return jsonify({"status": {"code": 500, "message": str(e)}, "data": None}), 500

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))