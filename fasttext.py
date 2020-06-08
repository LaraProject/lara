from gensim.models import FastText
from gensim.models.callbacks import CallbackAny2Vec
import argparse
import time


class EpochLogger(CallbackAny2Vec):
  '''Callback to log information about training'''
  def __init__(self):
    self.epoch = 0
    self.t0 = time.time_ns()
    self.curbatch = 0
    
  def on_batch_begin(self, model):
    total = model.corpus_total_words
    batch_size = model.batch_words
    number_of_batch = total/batch_size
    self.curbatch += 1
    dt = (time.time_ns() - self.t0) / 1e9
    dt /= self.curbatch
    eta = max(0, round((number_of_batch - self.curbatch) * dt, 2))
    n = 25
    ratio = (self.curbatch / number_of_batch)*n
    dots = "."*(n-int(ratio))
    done = "#"*(int(ratio))
    print(f"\r[{done}{dots}] ({eta} s - {self.curbatch}/{int(number_of_batch)})" + " "*6, end='', flush=True)
    
  def on_epoch_begin(self, model):
    self.epochs = model.epochs
    self.t0 = time.time_ns()
    self.curbatch = 0
    print("Epoch {}/{}".format(self.epoch+1, self.epochs))
    #print(model.__dict__)

  def on_epoch_end(self, model):
    dt = (time.time_ns() - self.t0) / 1e9
    eta = (self.epochs - self.epoch - 1) * dt
    eta_min = int(eta // 60)
    eta_s = round(eta % 60, 2)
    print("")
    print(f"Duration: {dt} s, ETA: {eta_min} minutes {eta_s} seconds")
    self.epoch += 1

data_path = ""

class MyIter(object):
  def __iter__(self):
    with open(data_path, 'r') as f:
      count = 0
      for line in f.readlines() + ["<unk> "*100 + "\n"]:
        if (count % 2) == 0:
          sentence = line[11:-1].split()
        else:
          sentence = ["<start>"] + line[9:-1].split() + ["<end>"]
        yield sentence
        count += 1

def train(aSize, aWindow, aMin_count, aWorkers, aEpochs, path, model_path):
  print("Building model...")
  model = FastText(size=aSize, window=aWindow, min_count=aMin_count, workers=aWorkers)
  print("Building vocab...")
  model.build_vocab(sentences=MyIter())
  total_examples = model.corpus_count
  print("Training at last...")
  model.train(sentences=MyIter(), total_examples=total_examples, epochs=aEpochs, callbacks=[EpochLogger()])
  model.wv.save_word2vec_format(path)
  if len(model_path) > 0:
    model.save(model_path)

# Argument management
argslist = argparse.ArgumentParser(description="FastText Word Vectors")

argslist.add_argument('data', metavar='facebook_data', type=str,
    help='Path to the data')
argslist.add_argument('--size', metavar='size', type=int,
    help='Specify the size of the word vectors', default=100, required=False)

argslist.add_argument('--window', metavar='size', type=int,
    help='Specify the size of the window', default=5, required=False)

argslist.add_argument('--minCount', metavar='count', type=int,
    help='Specify the minimum amount of occurence of a word', default=20, required=False)

argslist.add_argument('--workers', metavar='num', type=int,
    help='Specify the number of threads dedicated to the training', default=2, required=False)

argslist.add_argument('--epochs', metavar='size', type=int,
    help='Specify the number of epochs for the algorithm', default=5, required=False)

argslist.add_argument('--path', metavar='path', type=str,
    help='Specify the export path of the word vectors', default='fasttext_model_kv.txt', required=False)

argslist.add_argument('--modelPath', metavar='path', type=str,
    help='Specify the export path of the FastText model', default='', required=False)

args = argslist.parse_args()

print("Start training")
data_path = args.data
train(args.size, args.window, args.minCount, args.workers, args.epochs, args.path, args.modelPath)