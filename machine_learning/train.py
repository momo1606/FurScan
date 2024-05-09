import numpy as np
from tensorflow.keras.applications.resnet50 import ResNet50
import tensorflow as tf
from tensorflow.keras.utils import image_dataset_from_directory as dataGen
from tensorflow.keras.layers import  Resizing, Rescaling, RandomFlip, RandomRotation
from tensorflow.image import flip_left_right, adjust_brightness, central_crop
from tensorflow.keras.models import Model

class TrainModel:

    def __init__(self, data_path, img_size) -> None:
        self.path = data_path
        self.img_size = img_size
        self.model = ResNet50(   include_top=False,
                            weights='imagenet',
                            input_tensor=None,
                            input_shape=(self.img_size,self.img_size,3),
                            pooling=None,
                            classes=3,
                            classifier_activation='softmax'
                        )
        #print("initalized: ", self.model.summary())
    
    def add_preprocessing_layers(self, flip = 'horizontal_and_vertical', rotation = 0.2):
        
        
        process_input = tf.keras.applications.resnet50.preprocess_input
        i = tf.keras.Input(shape=(640, 640, 3))
        x = Resizing(self.img_size, self.img_size)(i)
        x = Rescaling(1./255)(x)
        x = RandomFlip(flip)(x)
        x = RandomRotation(rotation)(x)
        #flip_and_rotate,
        x = process_input(x)
        x = self.model(x, training = True)
        #core = self.model.output
        x = tf.keras.layers.GlobalAveragePooling2D()(x)
        x = tf.keras.layers.Dense(512, activation='relu')(x)
        #x = tf.keras.layers.Dropout(0.3)(x)
        predictions = tf.keras.layers.Dense(3, activation='softmax')(x)

        
        opt = tf.keras.optimizers.SGD(learning_rate = 0.001)
        
        self.model = Model(inputs = i , outputs=predictions)
        #print("after adding preprocessing layers: ",self.model.summary())
        self.model.compile(optimizer=opt,
              loss=tf.keras.losses.CategoricalCrossentropy(),
              metrics=['accuracy'])
        


    def train(self):
        
        datagen = dataGen(
            self.path,
            labels='inferred',
            label_mode='categorical',
            class_names=['bacterial','fungus','hypersensitivity'],
            color_mode='rgb',
            batch_size=1,
            image_size=(self.img_size, self.img_size),
            shuffle=True,
            seed=42,
            validation_split=None,
            subset=None
        )

        history = self.model.fit(
                                datagen,
                                epochs=30
                            )
        try:
            self.model.save("model/resnet50", save_format = 'tf')
        except:
            pass
        try:
            self.model.save("resnet.h5", save_format = 'h5')
        except: pass
        return


if __name__ == '__main__':

    TM = TrainModel()
    TM.add_preprocessing_layers(rotation=0.3)
    TM.train()

    print(tf.config.list_physical_devices('GPU'))
