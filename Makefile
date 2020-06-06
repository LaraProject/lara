#
# Makefile to call protocol buffer compiler for Java/Python
#

CC          := protoc
SRC         := message.proto

PYTHON_OUT  := python/
JAVA_OUT    := src/main/java/

all:
	$(CC) $(SRC) --python_out=$(PYTHON_OUT)
	$(CC) $(SRC) --java_out=$(JAVA_OUT)

clean:
	$(RM) $(PYTHON_OUT)/message_pb2.py
	$(RM) $(JAVA_OUT)/org/lara/rnn/Message.java
