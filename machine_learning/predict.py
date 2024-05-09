import numpy as np
import tensorflow as tf
import cv2


class Prediction:

    def __init__(self, model_path) -> None:
        self.model = tf.keras.models.load_model(model_path)
        

    def predict(self, image_path):
        try:
            image = cv2.imread(image_path)
        except: image = image_path
        print(image.shape)
        image = cv2.resize(image, (640,640))
        image = np.expand_dims(image, axis=0)
        prediction = self.model.predict(image)
        return(prediction) 


if __name__ == "__main__":

    pd = Prediction()
    pd.predict()