from gensim.models import FastText
import argparse

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
  model = FastText(size=aSize, window=aWindow, min_count=aMin_count, workers=aWorkers)
  model.build_vocab(sentences=MyIter())
  total_examples = model.corpus_count
  model.train(sentences=MyIter(), total_examples=total_examples, epochs=aEpochs)
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