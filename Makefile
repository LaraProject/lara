#
# Makefile to call protocol buffer compiler for Java/Python
#

CC          := protoc
SRC         := message.proto

PYTHON_OUT  := python/message_pb2.py
JAVA_OUT    := src/main/java/

all: $(PYTHON_OUT) $(JAVA_OUT)

$(PYTHON_OUT): $(SRC)
	$(CC) $(SRC) --python_out=$(dir $@)

$(JAVA_OUT): $(SRC)
	$(CC) $(SRC) --java_out=$(dir $@)

clean:
	$(RM) $(PYTHON_OUT)
	$(RM) src/main/java/org/lara/rnn/Message.java
