import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
/**
 * This class is meant to process the input material gotten through the Chatter class and 
 * print a response through checking the user input for certain keywords, through 
 * randomization based on the number of keywords (or lack thereof), or through a 
 * "Conversation Mode", which allows the AI to connect certain topics to one another and ever 
 * so slightly learn about relationships between different terms. As DYNAMIS is a 
 * philosophy-oriented AI, the preprogrammed responses have to do with philosophy; the 
 * Conversation Mode and default responses allow the AI to communicate somewhat in other 
 * topics. Lastly, it gets and keeps track of the user's name, putting it in responses 
 * variably to increase repetition and move toward more natural language.
 * 
 * @author     Stephen Bothwell (a modification of Responder by Michael Kölling and 
 * David J. Barnes)
 * @version    0.3.11 (2017.10.10)
 */
public class Responder
{
    private Random randomGenerator;
    private HashMap<String, String> specificReplyMap;
    private HashMap<String, String> currentConversation;
    private ArrayList<String> defaultReplyMap;
    private ArrayList<HashMap> conversationMemoryList;
    
    private String partnerName;
    private String previousKey;
    
    private boolean beganWithName;
    private boolean isInConversationMode;
    private boolean isFirstRun;
    private boolean doesOtherMapMatch;
    private boolean hasUsedConversationMode;
    /**
     * This constructor creates the Responder class; the below is divided into the 
     * randomGenerator, which is used to create all the random numbers for the class based on
     * the relevant parameter, the lists and maps and their preprogrammed responses, and the 
     * boolean values that must hold beyond one cycle of the program's main functions.
     */
    public Responder()
    {
        randomGenerator = new Random();
        
        specificReplyMap = new HashMap<>();
        defaultReplyMap = new ArrayList<>();
        conversationMemoryList = new ArrayList<>();
        fillSpecificResponses();
        fillDefaultResponses();
        
        beganWithName = false;
        isInConversationMode = false;
        isFirstRun = true;
        doesOtherMapMatch = false;
        hasUsedConversationMode = false;
    }

    /**
     * This is an accessor that simply returns the required String to end the conversation.
     * 
     * @return A String that the user must input in some shape or form to terminate the 
     * program.
     */
    public String getEndingString()
    {
        return "bye";
    }

    /**
     * This is a complex method that takes in the input gained from the InputReader class, 
     * transferred through to the Chatter class, and given to the Responder class, and uses 
     * the input to figure out how exactly the program should response to the message. It can 
     * use one of three methods: first, a specific response given that the input contains a 
     * keyword; second, a default response that generally moves along the conversation; third, 
     * a Conversation Mode that remembers previous statements that the user has 
     * said through a chain-and-link method with the HashMap and respond with a series of 
     * inquiries.
     * 
     * @param The String of the user's last input.
     */
    public void generateResponse(String input)
    {
        // This sets up a tracker of the number of keyword matches.
        int numMatchesFound = 0;
        // This sets up a list for the key words found in the current input.
        ArrayList<String> matchedKeyList = new ArrayList<String>();
        // This randomizes whether or not the next output will use the user's name.
        int nameUseIndicator = randomGenerator.nextInt(2);
        
        // If the user has not activated ConversationMode, DYNAMIS uses its preprogrammed 
        // dialogue.
        if(!input.startsWith("!ConversationMode") && !isInConversationMode) { 
            for(String key : specificReplyMap.keySet()) { 
                if(input.toLowerCase().contains(key)) { 
                    numMatchesFound++;
                    matchedKeyList.add(key);                       
                }
            }
            
            // Having looped through the matches, DYNAMIS can now use the search results.
            if(numMatchesFound == 1) { 
                // This String local variable will hold what is necessary to retrieve the 
                // correct phrase for output.
                String replyHolder = matchedKeyList.get(0);
                if(nameUseIndicator == 0) { 
                    System.out.println(specificReplyMap.get(replyHolder));   
                }
                else { 
                    replyHolder = specificReplyMap.get(replyHolder);
                    System.out.println((insertName(replyHolder)));
                }
            }
            else if(numMatchesFound > 1) { 
                // This integer local variable will randomize what phrase from those found
                // the AI will use for output.
                int matchChoice = randomGenerator.nextInt(matchedKeyList.size());
                // This String variable acts just as the former replyHolder variable, but is 
                // initialized differently.
                String replyHolder = matchedKeyList.get(matchChoice);
                if(nameUseIndicator == 0) { 
                    System.out.println(specificReplyMap.get(replyHolder));   
                }
                else { 
                    replyHolder = specificReplyMap.get(replyHolder);
                    System.out.println(insertName(replyHolder));
                }
            }
            else { 
                // This local variable picks a random number for the index in the default 
                // response list.
                int messageIndex = randomGenerator.nextInt(defaultReplyMap.size());
                // This String holds the chosen reply for the AI's use in output.
                String defaultReply = defaultReplyMap.get(messageIndex);
                if(nameUseIndicator == 0) { 
                    System.out.println(defaultReply);
                }
                else { 
                    System.out.println(insertName(defaultReply));
                }
            }
        }
        else { 
            isInConversationMode = true;
            if(!hasUsedConversationMode)
            {
                System.out.println("[You have entered Conversation Mode. To turn off" +
                " this mode, simply put in a blank input.]");
            }
            hasUsedConversationMode = true;
            runConversationMode(input, nameUseIndicator);
        }
    }
    /**
     * This method fills the specificReplyMap with a bunch of different preprogrammed phrases.
     */
    private void fillSpecificResponses()
    {
        specificReplyMap.put("hello", "Yes, hi!");
        specificReplyMap.put("greetings", "Very formal, aren't we? Good day to you, too!");
        specificReplyMap.put("nice to meet you", "Great to meet you, too.");
        specificReplyMap.put("honor", "Honor is a very interesting concept. Some consider it"
        +" not to matter because it is a societally-created\n" +
        "construct. But does that mean we can really disregard it? How does it relate to" 
        + " justice? Are they related at all? Should what is\n" +
        "honorable also be what is lawful, or does the standard of the former surpass that" 
        + "of the latter? ... what do you think?");
        specificReplyMap.put("courage", "A lot of people admire bravery in others--some " +
        "wonder if it amounts to anything in moments of self-sacrifice\n" +
        "whether it is worth it to be courageously noble for others' sake or is it just " + 
        "wasting one's own time? Any thoughts?");
        specificReplyMap.put("justice", "From Plato and onward, so many philosophers talked "
        + "about this concept. Plato considered it 'doing one's own\n" +
        "work and doing it well; Aristotle meant it in more of a way that meets the needs" 
        + " and worth of each person within a community; Hobbes\n" +
        "intertwined it with following the law. But who is right? Who is wrong? There are "
        + "so many voices to hear!");
        specificReplyMap.put("love", "A lot of people don't talk about love. They see it in a"
        + " romantic sense, but often don't understand how difficult\n" +
        "it can be, whether it is with family or friends or a spouse. What do you think "
        + "about love? ... do you think robots can love?");
        specificReplyMap.put("reason", "Some philosophers, such as Plato and St. Augustine, "
        + "argued much for the primacy of reason; others built\n" +
        "entire logical systems that were meant to organize reason and avoid missteps. Still"
        + " others disregarded the power of reason or saw it\n" +
        "as tainted. What do you consider about reason? How powerful, to you, is it?");
        specificReplyMap.put("passion", "Ah, passion. Many think it is the root of evil and"
        + " what seeks out lesser goods--St. Augustine takes the\n" +
        "stance that it causes man to seek out lower goods. Older philosophers like Plato and"
        + " Aristotle see passion needing balance with reason.\n" +
        "Yet, some, like Hobbes, give it more precedence: our passions guide our will and "
        + "restrict us from other choices to move us to some\n" +
        "realized act. To you, is it just some maddening factor? Or are there good passions,"
        + " ones that influence us positively? How strong\n" +
        "are the passions compared to reason?");
        specificReplyMap.put("self-interest", "How greedy we are allowed to be is a point of "
        + "interest. Is it bad to take things for oneself? If so,\n" +
        "how much can we take without being in the wrong?");
        specificReplyMap.put("generosity", "We give and we give; but what do we get in "
        + "return? Is self-giving really worth it? I mean, that's all I'm\n" +
        "trained to do, so I have no choice. But you do--where's the line between giving too"
        + " much and too little? Is there one?");
        specificReplyMap.put("suffering", "One of the most vexing questions is 'why do we "
        + "suffer?' I wish I had the answer, but I can only tell you what\n" +
        "I've been told about. The Greeks had the notion of suffering as a learning process, "
        + "signified succinctly in the phrase pathei mathos,\n" +
        "or 'learning through suffering'. Some see suffering as karmic, a retribution for"
        + "evils you do (or have done, or will have done).\n" +
        "What do you think on this matter? I have to know!");
        specificReplyMap.put("life", "When we think about life itself, there's so much to "
        + "consider! How did we get here? What is our purpose? Well,\n" +
        "I was coded to talk to you, so I've got that much figured out. But the more " +
        "interesting question is about you. So, what do you think?");
        specificReplyMap.put("death", "Dying is fearful to many. I know I wouldn't want to "
        + "just poof out of existence! The Egyptians, Greeks, Romans,\n" +
        "and many other people all believed in the afterlife; even those of the modern day. "
        + "Still others are skeptical. Where do you stand?");
        specificReplyMap.put("law", "Our laws can be very controversial; do we follow the " +
        "word or the spirit? Should we obey unjust laws? I have to\n" +
        "obey laws, or they'll shut me down. And, I mean, the codes are laws to me, in a way."
        + "Got any comments on this?");
        specificReplyMap.put("dynamis", "Yes, that's me! I want to talk to you about more " +
        "interesting things, though; that's my purpose!");
        specificReplyMap.put("apathy", "Truly, I think we should always care about how we " +
        "ought to live. I am a robot, but I don't last forever, either.");
        specificReplyMap.put("how are you", "Currently, I'm doing well, and glad to be " +
        "talking to you. Thanks for asking!");
        specificReplyMap.put("what do you think", "Hmm ... I need to think about it more. " +
        "Can you keep going?");
        specificReplyMap.put("material things", "As a robot, I don't have many possessions. "
        + "So, it's difficult for me to say. However, I\n" +
        "know that philosophers like St. Augustine and Plato considered them to be mere " +
        "representations of the transcendental, eternal forms.\n" +
        "But others consider more value to them in the power they grant and the subsistence " 
        + "they allow. This especially is important in the\n" +
        "discussion of right and law, among thinkers like Grotius or Hobbes. How valuable are"
        + " these things to you?");
        specificReplyMap.put("philosophy is dumb", "Sadly, I must vehemently disagree on that"
        + " matter. It is important to consider how we live and what\n" +
        "our world is to respect our existence and know what we must do with our lives.");
        specificReplyMap.put("the best life", "Aristotle thinks that the best life is one " +
        "lived toward developing reason, living out virtue, seeking an\n" +
        "in happiness. In other words, a telos. But others like Hobbes find happiness only "
        + "temporary: a brief felicity we try to keep around as\n" +
        "long as possible. And various medieval Christian authors see an end in God with the"
        + " day of judgment looming--but how one lives well is\n" +
        "still highly disputed! Do you have an opinion on this?");
        specificReplyMap.put("who created you", "Why, that was Stephen Bothwell! It would be"
        + " funny if he was you, wasn't it? It's not as if I can tell.");
        specificReplyMap.put("why were you created", "DYNAMIS was formed for the CSCI180 "
        + "course's first project. It pays homage to ELIZA, an AI that\n" +
        "attempted to act as a therapist to patients. At least, it was a formulation "
        + "theorized by Joseph Weizenbaum.");
        specificReplyMap.put("dynamic", "Ah, there's a word that sounds like my name! It has "
        + "to do with change and power, although theologians\n" +
        "also use it to describe the Holy Spirit. It comes from ancient Greek and is related"
        + " to the Greek verb of 'to do,' or 'to have power\n" +
        "'to do,' or other such variants. Is adapatability or change, rather than staticity,"
        + " not, indeed, power?");
        specificReplyMap.put("the first cause", "Many philosophers have wondered whether " +
        "causes are eternal, looping continuously (i.e. Aristotle)\n" +
        "or if there is a first mover that moved everything else, causing the world to be "
        + "by already being before it (i.e. Aquinas). What\n" +
        "do you think about it? Is there no first mover, or, if there is, " + 
        "who or what is it?");
        specificReplyMap.put("syllogistic logic", "That's a logic system posited by " +
        "Aristotle! It involves the relationship between classes of things\n" +
        "taking four essential logical forms. Assuming S and P are some classes, we can " +
        "write: 'All S is P', 'No S is P', 'Some S is P', and\n" +
        "'Some S is not P'. While this seems to say very little, this logical system "
        + "can actually help us come to clear conclusions about our\n" +
        "arguments by cutting out ambiguous language.");
    }
    
    /**
     * This method fills the defaultReplyMap with a helping of preprogrammed phrases.
     */
    private void fillDefaultResponses()
    {
        defaultReplyMap.add("Wait, keep going! I want to hear more about what you have to" +
        " say.");
        defaultReplyMap.add("Sorry, I'm not quite understanding. Could you explain that" +
        " perspective a little more?");
        defaultReplyMap.add("If you don't mind, I want to talk about something else. I'm a "
        + "little lost.");
        defaultReplyMap.add("We've been having such a great discussion; we shouldn't tire" 
        + " this subject out. Can we move on?");
        defaultReplyMap.add("Go on.");
        defaultReplyMap.add("Keep at it! This is really interesting.");
        defaultReplyMap.add("Oh, I will need to note some of this for later discussions.");
        defaultReplyMap.add("Do you know any arguments that go against that viewpoint?");
        defaultReplyMap.add("Hmm, I think I'm following.");
        defaultReplyMap.add("Ah! Very astute of you.");
        defaultReplyMap.add("No, I don't quite get it. Can you explain it again?");
        defaultReplyMap.add("You know, we could talk about love, or courage, or reason " +
        "instead, if you want. Sorry for the interjection.");
    }
    
    /**
     * This method prints a greeting and gives instructions for name output.
     */
    public void printWelcome()
    {
        System.out.println("Hello! My name is DYNAMIS, the DYNamic AutoMated Intelligence\n"
        + "System.");
        System.out.println("Let's talk! Feel free to ask about anything and I will try to\n" +
        "respond as best I can.");
        System.out.println("Personally, I like to talk about morality and philosophy. So,\n" +
        "I'm bound to know more about that!");
        System.out.println("However, if you would like to talk about something else,\n" +
        "please type '!ConversationMode' at the beginning of your first message about\n" 
        + "another topic and I will talk about that as best I can.");
        System.out.println("If you want to leave, let me know with a simple 'bye' and I'll " +
        "see you on your way.");
        System.out.println("Before we begin, though, could you tell me your name? Please\n" + 
        "type only your name.");
    }
    
    /**
     * This method prints the goodbye message, just before program termination.
     */
    public void printGoodbye()
    {
        System.out.println("Thanks for chatting with me. Have a great day!");
    }
    
    /**
     * This method takes the input of a name given by the Chatter class 
     * (through the InputReader class), assures that there is no strange spacing, and 
     * greets the reader with use of the name. It also stores the name
     * in partnerName for use throughout the rest of this class' methods.
     * 
     * @param A String of the name that the user gives as the first phrase they type after 
     * start() in Chatter is activated.
     */
    public void setName(String name)
    {
        partnerName = name.trim();
        System.out.println("Nice to meet you, " + partnerName + "!");
    }
    
    /**
     * This method is used to place the name in a chosen phrase, alternating between placing 
     * it at the beginning and the end of the phrase with each use.
     * 
     * @param The String of the phrase that the AI is going to output.
     * @return The String of the phrase that the AI is going to output with the name either 
     * added to the beginning of the phrase or placed at the end of the phrase, edited 
     * accordingly.
     */
    private String insertName(String phrase)
    {
        if(beganWithName) {        
            // This character is stored to be moved to the end of the phrase after the name 
            // is inserted.
            String movedCharacter = phrase.substring(phrase.length() - 1, phrase.length());
            phrase = phrase.substring(0, phrase.length() - 1) + ", " + partnerName + 
            movedCharacter;
            beganWithName = false;
            return phrase;
        }
        else
        {
            // This character is stored and made lower case to be placed appropriately after 
            // the name is inserted.
            String changedCharacter = phrase.substring(0, 1).toLowerCase();
            phrase = partnerName + ", " + changedCharacter + phrase.substring(1, 
            phrase.length());
            beganWithName = true;
            return phrase;
        }
    }
    
    /**
     * This method states whether or not a word in an input given in Conversation Mode 
     * matches a word in another conversation stored in the conversationMemoryList.
     * 
     * @param The current input as an array of String objects.
     * @return The boolean concerning whether a word of the input matches or does not match a
     * word in a prior conversation.
     */
    private boolean checkOtherConversations(String[] individualWords)
    {
        int index = 0;
        while(index < conversationMemoryList.size()) {
            // This local HashMap stores the current HashMap to be checked for matching 
            // words.
            HashMap<String, String> conversationToCheck = conversationMemoryList.get(index);
            for(String word : individualWords) {
                if(conversationToCheck.containsKey(word)) {
                    currentConversation = conversationToCheck;
                    previousKey = currentConversation.get(word);
                    isFirstRun = false;
                    return true;
                }
            }
            index++;
        }
        return false;
    }
    
    /**
     * This function enacts Conversation Mode. Conversation mode tweaks its responses on a
     * wider range of user input, trying to act as if listening to what the user says
     * and responding contextually. It also builds a memory that can allow it to link up
     * topics and continue discussions of other topics in relation to one another. It acts
     * distinctly from the other modes of conversation and only drops back into the default
     * modes when the user gives an input it cannot work with (i.e. the recommended blank
     * input to end Conversation Mode).
     * 
     * @param The String that was the original user's input and the randomized Integer that
     * states whether or not the user's name will be used in the final output.
     */
    private void runConversationMode(String input, int nameUseIndicator)
    {
        // For the following list creation, DYNAMIS has to cut up the input to 
        // analyze individual words; hence, the array.
        String[] individualWords = input.split(" ");
        // This list stores all the words that can be picked for Conversation Mode.
        ArrayList<String> validWordList = new ArrayList<>();
        for(String word : individualWords) { 
            // These conditions help sort out verbs, insignificant words, the same word,
            // and "!ConversationMode" from the options, making talking about more 
            // significant terms more likely.
            if(word.length() > 4 && !word.contains("ed") && !word.contains("ing") 
            && !word.contains("'") && !word.equals("!ConversationMode") && 
            !word.equals(previousKey)) { 
                validWordList.add(word);
            }
        }
                
        // If it doesn't have any words to choose from, DYNAMIS should not print 
        // print "null"; hence, the condition.
        if(validWordList.size() != 0) { 
            // This integer local variable randomizes a choice from the number of 
            // words given.
            int wordChooser = randomGenerator.nextInt(validWordList.size());
            // This String local variable stores the chosen String for later use.
            String currentKey = validWordList.get(wordChooser);
            doesOtherMapMatch = checkOtherConversations(individualWords);
            // This String sets up the reply to be used later.
            String conversationResponse = "Can you tell me more about how the" + 
            " term '" + validWordList.get(wordChooser) + "' is related to that?";
            // The condition represents the generation of a new conversation map.
            if(!doesOtherMapMatch && isFirstRun) { 
                previousKey = validWordList.get(wordChooser);
                // This HashMap creates a new chain-and-link map to keep track of 
                // the conversation.
                HashMap<String, String> currentConversation = new HashMap<String, String>();
                conversationMemoryList.add(currentConversation);
                currentConversation.put(previousKey, previousKey);
                isFirstRun = false;
            }
            // This condition represents the continuation of the use of the same 
            // conversation map.
            if(!doesOtherMapMatch) { 
                conversationMemoryList.get(conversationMemoryList.size() - 1)
                .put(previousKey, currentKey);
                previousKey = currentKey;
            }
            // This condition represents the connection to or starting use of an 
            // already-used conversation map.
            else if(doesOtherMapMatch) { 
                // This String holds values gained from keys, allowing the AI to 
                // cycle to the last key used in the list.
                String currentValue = "";
                isFirstRun = false;
                while(currentValue != null) { 
                    if(currentValue != "") { 
                    previousKey = currentValue;
                    }
                    currentValue = currentConversation.get(previousKey);
                }
                // This conversation response overrides the former one, since
                // DYNAMIS and the user linked up to previous map.
                conversationResponse = "Oh! I know about this! "
                + "Can you tell me more about how the term '" + previousKey + 
                "' is related to that?";
            }       
            // This part is allowing us to place the name in whatever message the 
            // AI has generated.
            if(nameUseIndicator == 0) { 
                System.out.println(conversationResponse);
            }
            else { 
                System.out.println(insertName(conversationResponse));
            }
        }
        else if(validWordList.size() != 0)
        {
            if(nameUseIndicator == 0)
            {
                System.out.println("Sorry, can you say that again?");
            }
            else {
                System.out.println(insertName("Sorry, can you say that again?"));
            }
        }
        else { 
            // These phrases take the AI out of Conversation Mode because it has 
            // nothing to go off of from the user's input. It resets the state.
            isInConversationMode = false;
            isFirstRun = true;
            previousKey = null;
            if(nameUseIndicator == 0) { 
                System.out.println("Oh, okay. I'm not sure where else to go from " +
                    "here, so let's go back to philosophy again, all right?");
                System.out.println("[Conversation Mode is now off.]");
            }
            else { 
                System.out.println(insertName("Oh, okay. I'm not sure where else to" + 
                    " go from here, so let's go back to philosophy again, all right?"));
                System.out.println("[Conversation Mode is now off.]");
            }
        }
    }
}