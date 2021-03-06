package Similarity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/** This demo shows user-provided sentences (i.e., {@code List<HasWord>})
 *  being tagged by the tagger. The sentences are generated by direct use
 *  of the DocumentPreprocessor class.
 *
 *  @author Christopher Manning
 */
public class Tagger { 
	
//	   String taggerPath = "models/english-left3words-distsim.tagger";
//	   MaxentTagger tagger = new MaxentTagger(taggerPath);
   public String pos(String str, MaxentTagger tagger) throws Exception{
//	   String taggerPath = "models/english-left3words-distsim.tagger";
//	   MaxentTagger tagger = new MaxentTagger(taggerPath);
	   String result="";
	   TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(),
										   "untokenizable=noneKeep");
	    DocumentPreprocessor documentPreprocessor = new DocumentPreprocessor(new StringReader(str));
	    documentPreprocessor.setTokenizerFactory(ptbTokenizerFactory);
	    for (List<HasWord> sentence : documentPreprocessor) {
	      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
	      result+=Sentence.listToString(tSentence, false); 
	    }

	return result;
	   
   }
  public static void main(String[] args) throws Exception {
	   String taggerPath = "models/english-left3words-distsim.tagger";
	  // String taggerPath = "models/english-bidirectional-distsim.tagger";
	   MaxentTagger tagger = new MaxentTagger(taggerPath);
	Tagger tt=new  Tagger();
    String text = "Zheng Jie (born 5 July 1983) is a Chinese professional tennis player. Her career high ranking is World No. 15 which she achieved on 18 May 2009. As of 9 June 2014, Zheng is ranked World No. 90 in singles and World No. 22 in doubles and is the 4th ranked Chinese player.Zheng is one of the most successful tennis players from China. She has won four WTA singles titles Hobart in 2005, Estoril, Stockholm in 2006, and Auckland in 2012. She has also won fifteen doubles titles, eleven of them with Yan Zi including Wimbledon and the Australian Open in 2006. She won the bronze medal in doubles with Yan Zi at the 2008 Beijing Olympics. Her career high doubles ranking is World No. 3. Zheng has reached the singles semi-finals at the 2008 Wimbledon Championships, defeating a World No. 1, Ana Ivanovic, in the process, becoming the first Chinese female player to advance to the semi-finals at a Grand Slam.";
    System.out.println(tt.pos(text,tagger)); 
  }
}
