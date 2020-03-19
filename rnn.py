#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Mar 11 23:37:12 2020

@author: aviallon
"""

import keras
from keras.layers import Dense, LSTM, Activation, LeakyReLU, Dropout, Input, merge
from keras.models import Sequential, Model
from keras.optimizers import Nadam
from keras.callbacks import ReduceLROnPlateau, EarlyStopping
from keras.utils import to_categorical
import numpy as np


batch_size = 512
n_symbols = 20
n = 100000

vect_len = 5

#X = to_categorical(np.array(X), n_symbols)
#Y = to_categorical(np.array(Y), n_symbols)
X = np.random.randint(1, n_symbols-2, (n, vect_len))
#Y = np.sum(X, axis=-1)
Y = np.concatenate((X[:n//2]-1, X[n//2:]+1), axis=0)
X = to_categorical(X, num_classes=n_symbols)
Y = to_categorical(Y, num_classes=n_symbols)

num_target = 4
target = to_categorical(np.array([1]*(n//2) + [2]*(n//2)), num_classes=num_target)


encoder_inpt = Input(shape=(vect_len, n_symbols))
encoder_tensor = LSTM(256)(encoder_inpt)
encoder_tensor = Dropout(0.4)(encoder_tensor)
# encoder_tensor = LeakyReLU()(encoder_tensor)
# encoder_tensor = LSTM(256)(encoder_tensor)
# encoder_tensor = Dropout(0.4)(encoder_tensor)
encoder_tensor = LeakyReLU()(encoder_tensor)

decoder_inpt = Input(shape=(vect_len, n_symbols))
decoder_tensor = LSTM(256)(decoder_inpt)
decoder_tensor = Dropout(0.4)(decoder_tensor)
# decoder_tensor = LeakyReLU()(decoder_tensor)
# decoder_tensor = LSTM(256)(decoder_tensor)
# decoder_tensor = Dropout(0.4)(decoder_tensor)
decoder_tensor = LeakyReLU()(decoder_tensor)

merged = merge.concatenate([encoder_tensor, decoder_tensor], axis=1)

dense = Dense(num_target)(merged)
dense = LeakyReLU()(dense)
dense = Dense(num_target)(dense)
dense = Activation('softmax')(dense)

model = Model(inputs=[encoder_inpt, decoder_inpt], outputs=[dense])

model.compile(
    loss='categorical_crossentropy',
    optimizer=Nadam(),
    metrics=['accuracy']
    )

model.summary()

model.fit(x=[X, Y], y=target,
          epochs=50,
          validation_split=0.2,
          batch_size=batch_size,
          callbacks=[
              EarlyStopping(monitor='val_loss', patience=8, restore_best_weights=True)
              ],
          workers=4,
          max_queue_size=50
          )