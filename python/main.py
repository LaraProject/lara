#!/usr/bin/env python3
import socket
import select
from time import sleep
import message_pb2
from google.protobuf.internal import encoder
import tensorflow as tf
from tensorflow.keras import preprocessing
import pickle
import numpy as np

## RNN part

# Load the inference model
def load_inference_models(enc_file, dec_file):
	encoder_model = tf.keras.models.load_model(enc_file)
	decoder_model = tf.keras.models.load_model(dec_file)
	return (encoder_model, decoder_model)

# Load the tokenizer
def load_tokenizer(tokenizer_file):
	with open(tokenizer_file, 'rb') as handle:
		tokenizer = pickle.load(handle)
	return tokenizer

def load_length(length_file):
	with open(length_file, "r") as f:
		data = ((f.read()).split(","))
	return int(data[0]), int(data[1])

# Talking with our Chatbot
def str_to_tokens( sentence : str, tokenizer, maxlen_questions):
	words = sentence.lower().split()
	tokens_list = list()
	for word in words:
		if word in tokenizer.word_index:
			tokens_list.append(tokenizer.word_index[word])
		else:
			tokens_list.append(tokenizer.word_index['<unk>'])
	return preprocessing.sequence.pad_sequences([tokens_list],
			maxlen=maxlen_questions, padding='post')

def answer(question, enc_model, dec_model, tokenizer, maxlen_questions, maxlen_answers):
	states_values = enc_model.predict(str_to_tokens(question, tokenizer, maxlen_questions))
	empty_target_seq = np.zeros((1, 1))
	empty_target_seq[0, 0] = tokenizer.word_index['<start>']
	stop_condition = False
	decoded_translation = ''
	while not stop_condition:
		(dec_outputs, h, c) = dec_model.predict([empty_target_seq]
				+ states_values)
		sampled_word_index = np.argmax(dec_outputs[0, -1, :])
		sampled_word = None
		for (word, index) in tokenizer.word_index.items():
			if sampled_word_index == index:
				decoded_translation += ' {}'.format(word)
				sampled_word = word

		if sampled_word == '<end>' or len(decoded_translation.split()) > maxlen_answers:
			stop_condition = True

		empty_target_seq = np.zeros((1, 1))
		empty_target_seq[0, 0] = sampled_word_index
		states_values = [h, c]

	return (decoded_translation[:-5])  # remove end w

### END RNN PART ###

PORT = 9987

def recvall(sock):
	BUFF_SIZE = 4096 # 4 KiB
	data = b''
	while True:
		part = sock.recv(BUFF_SIZE)
		data += part
		if len(part) < BUFF_SIZE:
			# either 0 or end of data
			break
	return data

def answer_command(question, enc_model, dec_model, tokenizer, maxlen_questions, maxlen_answers):
	command = message_pb2.Command()
	command.type = message_pb2.Command.ANSWER
	command.name = 'ANSWER to "' + question + '"' 
	command.data = answer(question, enc_model, dec_model, tokenizer, maxlen_questions, maxlen_answers)
	return command

def main():
	# Connect over TCP socket
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.bind(('localhost', PORT))
	sock.listen(5)
	# Current person
	max_lengths = [[22,74]]
	person = 1
	enc_model, dec_model = load_inference_models("../models/" + str(person) + "/model_enc.h5", "../models/" + str(person) + "/model_dec.h5")
	tokenizer = load_tokenizer("../models/" + str(person) + "/tokenizer.pickle")
	maxlen_questions, maxlen_answers = load_length("../models/" + str(person) + "/length.txt")
	cmd = message_pb2.Command()
	over = False
	while True and (not over):
		conn, addr = sock.accept()
		#conn.setblocking(0)
		while True:
			data = conn.recv(4096)
			if not data: break
			ready = select.select([conn], [], [], 1.0)
			if ready[0]:
				data += recvall(conn)
			cmd.ParseFromString(data)
			if (cmd.type == message_pb2.Command.CommandType.QUESTION):
				print("Question : '" + cmd.data + "' received.")
				conn.send(answer_command(cmd.data, enc_model, dec_model, tokenizer, maxlen_questions, maxlen_answers).SerializeToString())
				print("Question answered.")
				conn.close()
				break
			elif (cmd.type == message_pb2.Command.CommandType.ANSWER):
				print("Error, only questions are accepted.")
				over = True
				conn.close()
				break
			elif (cmd.type == message_pb2.Command.CommandType.SWITCH_PERSON):
				print("Switching to person" + cmd.data)
				person = int(cmd.data)
				enc_model, dec_model = load_inference_models("../models/" + str(person) + "/model_enc.h5", "../models/" + str(person) + "/model_dec.h5")
				tokenizer = load_tokenizer("../models/" + str(person) + "/tokenizer.pickle")
				maxlen_questions, maxlen_answers = load_length("../models/" + str(person) + "/length.txt")
				conn.close()
				break
			elif (cmd.type == message_pb2.Command.CommandType.SHUTDOWN):
				print("Quiting.")
				over = True
				conn.close()
				break
			sleep(1)
	sock.close()

if __name__ == '__main__':
	main()
