import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;


public class OpenNLP {
	public static String[] SentenceDetect(String paragraph) throws InvalidFormatException, IOException {
		//String paragraph = "Hi. How are you? This is Alex.";
		InputStream modelIn = new FileInputStream("/home/alex/Dropbox/WorkSpace/OpenNLPDanwei/src/en-sent.bin");
		SentenceModel model = new SentenceModel(modelIn);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		
		String[] sentences = sdetector.sentDetect(paragraph);

		modelIn.close();
		return sentences;
	}
	
	public static List<List<String[]>> POSTag(String paragraph) throws IOException {
		String[] sentences = OpenNLP.SentenceDetect(paragraph);
		POSModel model = new POSModelLoader().load(new File("/home/alex/Dropbox/WorkSpace/OpenNLPDanwei/src/en-pos-maxent.bin"));
		POSTaggerME tagger = new POSTaggerME(model);
		List<List<String[]>> lists = new ArrayList<List<String[]>>();
		List<String[]> tagslists = new ArrayList<String[]>();
		List<String[]> tokenslists = new ArrayList<String[]>();
		lists.add(tokenslists);
		lists.add(tagslists);
		for (String sentence : sentences) {
			//ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(sentence));
			//String line;
			//while ((line = lineStream.read()) != null) {
				String[] whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
				String[] tags = tagger.tag(whitespaceTokenizerLine);
				tokenslists.add(whitespaceTokenizerLine);
				tagslists.add(tags);
			//}
		}
		return lists;
	}
	
	public static List<Parse[]> Parsepa(String paragraph) throws InvalidFormatException, IOException {
		InputStream modelIn = new FileInputStream("/home/alex/Dropbox/WorkSpace/OpenNLPDanwei/src/en-parser-chunking.bin");
		ParserModel model = new ParserModel(modelIn);	 
		Parser parser = ParserFactory.create(model);
		String[] sentences = OpenNLP.SentenceDetect(paragraph);
		List<Parse[]> parseslists = new ArrayList<Parse[]>();
		for (String sentence : sentences) {
			Parse[] topParsers = ParserTool.parseLine(sentence, parser, 1);
			parseslists.add(topParsers);
		}
		return parseslists;
	}
	
	public static void Parse(String sentence) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream("/home/alex/Dropbox/WorkSpace/OpenNLPDanwei/src/en-parser-chunking.bin");

		ParserModel model = new ParserModel(is);
	 
		Parser parser = ParserFactory.create(model);
	 
		Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
	 
		for (Parse p : topParses)
			p.show();
	 
		is.close();
	}
}
