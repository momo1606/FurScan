import numpy as np
import tensorflow as tf
import cv2
import matplotlib.pyplot as plt
from sklearn.metrics import classification_report, confusion_matrix
import os

class ImageClassifierTester:

    def __init__(self, model_path, test_images_folder, test_labels):
        self.model = tf.keras.models.load_model(model_path)
        self.test_images_folder = test_images_folder
        self.test_labels = test_labels
        self.test_data = self.load_test_data()

    def preprocess_image(self, image):
        image = cv2.resize(image, (640, 640))
        image = np.expand_dims(image, axis=0)
        return image

    def load_test_data(self):
        
        test_data = []


        for filename in os.listdir(self.test_images_folder):
            if filename.endswith(".jpg"):  # Assuming the test images are in JPEG format
                
                image_path = os.path.join(self.test_images_folder, filename)
                image = cv2.imread(image_path)
                image = self.preprocess_image(image)
                test_data.append(image)

       
        test_data = np.array(test_data)


        print("Shape of test_data:", test_data.shape)
        return test_data

    def predict_images(self):
        predictions = []
        for image in self.test_data:
            prediction = self.model.predict(image)
            predictions.append(prediction)
        return predictions

    def evaluate_model(self):
        predictions = self.predict_images()
        predicted_labels = [np.argmax(pred) for pred in predictions]
        true_labels = [np.argmax(label) for label in self.test_labels]


        accuracy = np.mean(np.equal(predicted_labels, true_labels))

     
        class_report = classification_report(true_labels, predicted_labels)

        conf_matrix = confusion_matrix(true_labels, predicted_labels)

        return accuracy, class_report, conf_matrix

    def visualize_results(self, num_images=5):
        predictions = self.predict_images()
        class_names = ["bacterial", "fungal", "hypersensitivity"]  

        for i in range(num_images):
            plt.figure(figsize=(6, 3))
            plt.subplot(1, 2, 1)
            plt.imshow(self.test_data[i][0])  
            plt.title("True Label: {}".format(class_names[np.argmax(self.test_labels[i])]))

            plt.subplot(1, 2, 2)
            plt.bar(range(4), predictions[i][0])  
            plt.xticks(range(4), class_names, rotation=45)
            plt.title("Predicted Label: {}".format(class_names[np.argmax(predictions[i][0])]))

            plt.tight_layout()
            plt.show()

if __name__ == "__main__":

    test_labels = [0,0,1,1,2,2]  

    model_path = "resnet.h5"  
    test_images_folder = ""

    tester = ImageClassifierTester(model_path, test_images_folder, test_labels)
    accuracy, class_report, conf_matrix = tester.evaluate_model()
    print("Accuracy: {:.2f}%".format(accuracy * 100))
    print("Classification Report:\n", class_report)
    print("Confusion Matrix:\n", conf_matrix)

 
    tester.visualize_results(num_images=6)