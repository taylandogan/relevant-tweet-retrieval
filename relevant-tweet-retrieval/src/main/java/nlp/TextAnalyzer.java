package nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TextAnalyzer {
	
	private final static String[] schemesForValidation = {"http", "https"};
	private static StanfordCoreNLP pipeline;
	
	public TextAnalyzer() {
    	// Create a StanfordCoreNLP object
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        TextAnalyzer.pipeline = new StanfordCoreNLP(props);
	}

	// Takes a text, returns a lemmatized clean word list
    public List<String> extractWordList(String text) {
    	// Initialize special prefixes
    	SpecialPrefixes sp = new SpecialPrefixes();
    	// Initialize URL validator
    	UrlValidator uval = new UrlValidator(schemesForValidation);
    	// Get the custom stop word set
        CustomStopWords csw = new CustomStopWords();
        Set<String> stopWords = csw.getStopWords();
    	// Get custom abbreviations
        CustomAbbreviations ca = new CustomAbbreviations();
        Set<String> abbreviations = ca.getAbbreviations();
        
        Annotation document = pipeline.process(text);
        List<String> extractedWords = new ArrayList<>();
        
        for(CoreMap sentence : document.get(SentencesAnnotation.class)){
        	// If a sentence starts with a special prefix, just skip it
        	if (sp.hasSpecialPrefix(sentence.toString())) {
        		continue;
        	}
        	
        	// Else attempt to tokenize, lemmatize, etc.
            for(CoreLabel token: sentence.get(TokensAnnotation.class)){       
                String word = token.word().toLowerCase().trim();
                
                // If the parsed word is a numeric, ignore it
                if(StringUtils.isNumeric(word)) {
                	continue;
                }
                
                // Eliminate Twitter mentions, e.g. @username
                if(word.startsWith("@")) {
                	continue;
                }
                
                // If the word is nothing but a punctuation, ignore it
                else if(Punctuation.removePunctuation(word).equalsIgnoreCase("")){
                	continue;
                }
              
                // Eliminate, if word is in stop word set
                else if (stopWords.contains(word)) {
                	continue;
                }
                
                // Eliminate, if word is in abbreviations set
                else if(abbreviations.contains(word)) {
                	continue;
                }
                
                // If the parsed word is a valid URL, ignore it
                else if(uval.isValid(word)) {
                	continue;
                }
                
                // If word passes all the tests, attempt to lemmatize
                else {
                	String lemma = token.lemma().toLowerCase().trim();
                	Punctuation.removePunctuation(lemma);
                    extractedWords.add(lemma);
                }
            }
        }
        
        return extractedWords;
    }

    public static void main(String[] args) {
    	
        String text = "It was Ms. Fitzhugh. She was walking fast. "
       		+ "A strange expression crossed the faces of the students as they glanced toward "
       		+ "the door and saw the principal go straight into the boys’ restroom. The footsteps stopped."
       		+ " There was a deep, throaty sound difficult to describe. Then came an eruption of shrill "
       		+ "screaming and a rapid sound of heels. Moments later, Ms. Fitzhugh emerged, her eyes wild. "
       		+ "Screaming, she skidded in the hall and headed toward the office.";
        
        String tweet1 = "We're proud to join 160+ companies in @HRC's petition to #RepealHB2,"
        		+ " because we believe in equal rights for all:";
        
        String tweet2 = "TODAY WAS MY GRANDPARENTS 65TH WEDDING ANNIVERSARY AND "
        		+ "I THREW FIRST PITCH AT @GoSquirrels  AND I DEADLIFTED 295 SO IT WAS A GOOD DAY.";
        
        String tweet3 = "Adding space…in space. Watch as BEAM module attaches to @Space_Station "
        		+ "tomorrow at 5:30am ET http://go.nasa.gov/1SgkTSv ";
        
        String tweet4 = "Donate to HRC for a chance to win 2 tickets to @springsteen's NYC show. "
        		+ "Enter on @crowdrise at http://crowdrise.com/nohate  #NoHateInMyState";
        
        String tweet5 = "Made the ray marcher into a 3.5 KB html page: http://doodle.notch.net/unmandelboxing/ "
        		+ "IE seems to dislike it.";
        
        String tweet6 = "Woke up before 8 am for the first time in a long time, "
        		+ "and I kind of like it. So many hours of sunlight ahead of me! Coffee? Need coffee.";
        
        String tweet7 = "I only follow'ed you bc I was waiting for you to finish http://cliffhorse.com . "
        		+ "Now, I follow bc you're damn funny.";
        
        String tweet8 = "lol afaik this is the best thing related to league of legends omg ahahah";
        String tweet9 = "I liked a @YouTube video http://youtu.be/Vi1tkUNpohM?a  Cologne Shop Roleplay (ASMR)";
        String tweet10 = "I'm at @AstirBeach in Vouliagmeni, Athens https://www.swarmapp.com/c/7lwQrizYC06";
       
      TextAnalyzer ta = new TextAnalyzer();
//      System.out.println(ta.extractWordList(text));
//      System.out.println(ta.extractWordList(tweet1));
//      System.out.println(ta.extractWordList(tweet2));
//      System.out.println(ta.extractWordList(tweet3));
//      System.out.println(ta.extractWordList(tweet4));
//      System.out.println(ta.extractWordList(tweet5));
//      System.out.println(ta.extractWordList(tweet6));
//      System.out.println(ta.extractWordList(tweet7));
//      System.out.println(ta.extractWordList(tweet8));
//      System.out.println(ta.extractWordList(tweet9));
//      System.out.println(ta.extractWordList(tweet10));
      
      
      // Politics-related tweets, chosen manually
//      String p1 = "BREAKING: @AP finds Trump reached the number of delegates needed to clinch the Republican nomination for president."; 
//      String p2 = "If Trump could \"break the laws of politics\" it was because the GOP wasn't behaving like a functional political party.";
//      String p3 = "VIDEO: @stephenatap explains how he found delegate 1,237 who put Trump over the top. http://apne.ws/22qDPBW ";
//      String p4 = "Trump has always been a Highly Respected voice in Politics. We need this kind of Thinking! #Trump2016 https://youtu.be/PRxl87wbCaY  via @YouTube";
//      String p5 = "Congress can be revived if it rises above electoral politics& goes back to ideological politics of Gandhi Nehru Patel &Azad @OfficeOfRG";
//      String p6 = "Being a feminist doesn't mean I have to like all women or agree with their politics but I will support all women in breaking the patriarchy.";
//      String p7 = "Putting politics over policy is dangerous and unacceptable. But that's what extremists in South Carolina just did:";
//      String p8 = "Congress has failed to act on emergency funding to protect Americans from #Zika  http://snpy.tv/1XuW5JY";
//      String p9 = "We must drive out the politics of despair & offer a vision for Britain and Europe - @johnmcdonnellMP. Agree? RT";
//      String p10 = "#2YearsOfModi: BJP's Ram Madhav speaks out. 'To have student politics on campus, is the right of any organisation.'";
//      String p11 = "Reject the politics of fear - and build a new Europe built in the interests of the majority: http://www.theguardian.com/commentisfree/2016/may/26/defeat-bigotry-engage-young-people-eu-zac-goldsmith ";
//      String p12 = "Opposition Leader Bill Shorten comforts Senator Nova Peris after she spoke on her departure from politics ";
//      String p13 = "Manafort isn't some low level aide - he's Trump's campaign manager! And he appears to have not the keenest grasp of how politics works";
//      String p14 = "Hero: Obama Blocks Sale Of Sacred Apache Land To Foreign Mining Firm – Universe Politics";
//      String p15 = "'If he wasn't PM, Dave would be campaigning to Leave', claims his best friend in politics, Steve Hilton. #LeaveEU";
//      String p16 = "The 2016 election, by the numbers and on the go: The CNN Politics app http://apple.co/1NUlHIq ";
//      String p17 = "One of the penalties for refusing to participate in politics is that you end up being governed by your inferiors. – Plato";
//      String p18 = "How current is government technology? GAO finds the Pentagon is still using floppies. http://apne.ws/1NN3EK1 ";
//      String p19 = "Obama's visit to Hiroshima speaks volumes about his presidency and the state of politics in Asia ";
//      String p20 = "Ex-PM Stephen Harper set to resign his seat before next fall to begin life after politics http://www.cbc.ca/news/politics";
//      String p21 = "Stephen Harper after trying 4 years to destroy social fabric of Canada leaves politics to make his fortune http://www.theglobeandmail.com/news/politics";
//      String p22 = "Will Jeremy Corbyn, the Bernie Sanders of the U.K., reshape his nation's politics or lead his party to irrelevance? ";
//      String p23 = "With all the years that Nigel Farage has spent in politics he's never outlined a cogent economic plan for Britain outside the EU #Incrowd";
//      String p24 = "China's official news agency says Taiwan's new president \"extreme\" in her politics because she's single. ";
//      String p25 = "BREAKING: Clinton wins Democratic primary in Washington. @AP race call at 11:24 p.m. EDT. #Election2016 #APracecall";
//      String p26 = "Europe's recent referendums reflect alienation from politics and anger at those who govern http://econ.st/1TlMe9f ";
//      String p27 = "Bernie Sanders' campaign asks Kentucky officials to review the vote in all 120 counties. http://apne.ws/1WQrZSp ";
//      String p28 = "#Trump is planning on taking out environmental regulations to boost oil and gas sector. Think that's smart politics? http://ow.ly/K3tX300D2G9 ";
//      String p29 = "New UK #politics #crowdfund campaign - 'Lexit The Movie: The left wing case for Brexit'";
//      String p30 = "#Politics Former Greek Finance Minister: The Swiss Should Vote 'Yes' On Basic Income ";
//      String p31 = "HILLARY CLINTON: Donald Trump is an 'urgent threat to our rights' http://read.bi/1WnOVrB";
//      String p32 = "EU referendum: Jean-Claude Juncker aide orders Britons not to vote for Boris Johnson | Politics | News | Daily ";
//      String p33 = "Politics › Obama's every gesture will be scrutinized in Hiroshima visit: Every gesture. Every word uttered or... http://bit.ly/20HZVP1 ";
//      String p34 = "Politics › Abe likely to announce Monday whether to raise sales tax: Prime Minister Shinzo Abe will likely an... http://bit.ly/1P1zmmY ";
//      String p35 = "Political liars destroying democracy: Brexit, George Osborne, and the art of post-factual politics http://www.spectator.co.uk/2016/05/";
//      String p36 = "Kentucky completes Democratic primary recanvass http://cbsn.ws/1TE6KlC  #politics #news";
//      String p37 = ".@JohnCassidy on the challenges facing Hillary Clinton in a general-election campaign against Donald Trump:";
//      String p38 = "Tanzanians nickname their new president \"the Bulldozer\". But his is government by gesture http://econ.st/20GQyyS ";
//      String p39 = "Israel's defence ministry has a familiar face at the wheel. And he's no safe pair of hands http://econ.st/1NPWwfO ";
//      String p40 = "Donald Trump is looking to capitalise on Secretary Clinton's e-mail missteps http://econ.st/1TDsApE ";
//      String p41 = "A British departure from the EU would be costly for the world's banks http://econ.st/1qLPpd2";
//      String p42 = "Voting wrongs: America's electoral laws are a recipe for chaos  http://econ.st/1Vk1ZNw ";
//      String p43 = "Is Britain's Chancellor right to say #Brexit would spark a recession? Join the debate live http://econ.st/2497Wx3 ";
//      String p44 = "It is past time for the world to get serious about North Korea's nuclear ambitions http://econ.st/1VjANhU ";
//      String p45 = "As next year's elections approach, can Kenya's government convince voters of its honesty? http://econ.st/244vK58 ";
//      String p46 = "Mississippi wants to be the 12th state to sue the Obama administration over bathroom guidance";
//      String p47 = "Big Guards-owned company may lose out as Iran economy, politics shift http://reut.rs/25nmfEc  via @Reuters #IRGC";
//      String p48 = "Increasingly vocal Hispanic minority speaking out in favor of Trump; resentment and suspicion from friends, family:";
//      String p49 = "House reverses course on LGBT rights, approving a policy of barring discrimination by federal contractors:";
//      String p50 = "VIDEO: Texas is fighting the Obama administration's order allowing transgender student use the bathroom of choice.";
      
      
      System.out.println(String.join(" ", ta.extractWordList(tweet1)));
      System.out.println(String.join(" ", ta.extractWordList(tweet2)));
      System.out.println(String.join(" ", ta.extractWordList(tweet3)));
      System.out.println(String.join(" ", ta.extractWordList(tweet4)));
      System.out.println(String.join(" ", ta.extractWordList(tweet5)));
    
    }
}
