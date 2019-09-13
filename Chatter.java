/**
 * This class creates a version of DYNAMIS, an artificial intelligence that can discuss 
 * philosophy (and other topics, to some degree) with the user. It is the main class for 
 * DYNAMIS, given the generic name "Chatter" for the AI. It cues the other classes, Responder 
 * and InputReader, to do most of the input and output work while overseeing their progress 
 * and cueing the end of the discussion when appropriate.
 * 
 * @author     Stephen Bothwell (a modification of the TechSupportv2 by Michael Kölling and 
 * David J. Barnes)
 * @version    0.3.10 (2017.10.09)
 */
public class Chatter
{
    private Responder responder;
    private InputReader reader;
    /**
     * This constructor creates the Chatter class by also forming objects of its heavily-used 
     * components: Responder and InputReader.
     */
    public Chatter()
    {
        responder = new Responder();
        reader = new InputReader();
    }

    /**
     * This is my attempt at running this project outside of BlueJ.
     */
    public static void main(String[] args)
    {
        Chatter chatter = new Chatter();
        chatter.start();
    }
    
    /**
     * This method activates DYNAMIS, causing the Terminal to pop up. It begins the 
     * conversation with the user, retrieves their name through the Responder class, and 
     * continues listening and responding to the user's statements until the user cues the 
     * end of the conversation.
     */
    public void start()
    {
        boolean finished = false;
        responder.printWelcome();
        String name = reader.getInput();
        responder.setName(name);
        while(!finished) {
            String input = reader.getInput();
            if(input.contains(responder.getEndingString())) {
                finished = true;
            }
            else {
                responder.generateResponse(input);
            }
        }
        responder.printGoodbye();
    }
}