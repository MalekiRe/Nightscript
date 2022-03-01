package rotn.nightscript.parser;

import java.util.ArrayList;

import static rotn.nightscript.parser.LexerToken.END_OF_FILE;
import static rotn.nightscript.parser.Phrase.*;

public class Parser {
    enum State {
        SHIFT,
        REDUCE,
        ACCEPT,
        ERROR
    }
    public static ArrayList<Pair<Phrase, ArrayList<ArrayList<Phrase>>>> phraseCombos = new ArrayList<>();
    static {
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(Phrase.FINISHED_COMPILATION, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(MULTI_EVENT);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(MULTI_EVENT, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(EVENT);
                phraseGroup.add(comma);
                phraseGroup.add(MULTI_EVENT);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(EVENT);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(EVENT, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(when_keyword);
                phraseGroup.add(identifier);
                phraseGroup.add(EVENT_ARGS_LIST);
                phraseGroup.add(left_curly_bracket);
                phraseGroup.add(MULTI_FUNCTION);
                phraseGroup.add(right_curly_bracket);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(EVENT_ARGS_LIST, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(left_square_bracket);
                phraseGroup.add(right_square_bracket);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(left_square_bracket);
                phraseGroup.add(EVENT_ARGS);
                phraseGroup.add(right_square_bracket);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(EVENT_ARGS, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(EVENT_ARG);
                phraseGroup.add(comma);
                phraseGroup.add(EVENT_ARG);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(EVENT_ARG);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(EVENT_ARG, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(identifier);
                phraseGroup.add(variable);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(event_lambda);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }

        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(MULTI_FUNCTION, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(FUNCTION);
                phraseGroup.add(semicolon);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(MULTI_FUNCTION);
                phraseGroup.add(MULTI_FUNCTION);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }

        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(FUNCTION, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(NORMAL_FUNCTION);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(EVENT_LAMBDA_FUNCTION);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(NORMAL_FUNCTION, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(identifier);
                phraseGroup.add(left_paren);
                phraseGroup.add(ARGS);
                phraseGroup.add(right_paren);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(event_lambda);
                phraseGroup.add(left_paren);
                phraseGroup.add(right_paren);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(EVENT_LAMBDA_FUNCTION, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(event_lambda);
                phraseGroup.add(left_paren);
                phraseGroup.add(ARGS);
                phraseGroup.add(right_paren);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(event_lambda);
                phraseGroup.add(left_paren);
                phraseGroup.add(right_paren);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(ARGS, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(ARGS);
                phraseGroup.add(comma);
                phraseGroup.add(ARGS);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(FUNCTION_ARG);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
        {
            Pair<Phrase, ArrayList<ArrayList<Phrase>>> myPair = Pair.of(FUNCTION_ARG, new ArrayList<>());
            ArrayList<ArrayList<Phrase>> phraseGroups = myPair.second;
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(FUNCTION);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(integer);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(string);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(float_var);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(double_var);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(bool);
                phraseGroups.add(phraseGroup);
            }
            {
                ArrayList<Phrase> phraseGroup = new ArrayList<Phrase>();
                phraseGroup.add(variable);
                phraseGroups.add(phraseGroup);
            }
            phraseCombos.add(myPair);
        }
    }
    static ArrayList<NodeToken> nodeTokens = null;
    public static int iterator = -1;
    public static NodeToken END_OF_FILE_TOKEN = new NodeToken(END_OF_FILE);
    public static NodeToken Parse(ArrayList<NodeToken> nodeTokens) {
        Parser.nodeTokens = nodeTokens;
        iterator = 0;
        return doParsing();
    }
    public static NodeToken getNextNodeToken() {
        iterator++;
        if(nodeTokens.size()+1 == iterator) {
            return END_OF_FILE_TOKEN;
        }
        return nodeTokens.get(iterator-1);
    }
    public static StringHolder stringHolder;
    public static NodeToken doParsing() {
        System.out.println(phraseCombos);
        State currState = State.SHIFT;
        ArrayList<NodeToken> stack = new ArrayList<>();
        NodeToken lookAheadToken = getNextNodeToken();
        while(currState != State.ACCEPT) {
            if(currState == State.ERROR) {
                System.out.println(stack);
                System.out.println(lookAheadToken);
                System.err.println("encountered error while parsing, something has gone horribly wrong");
                return null;
            }
            if(currState == State.SHIFT) {
                if(lookAheadToken != END_OF_FILE_TOKEN) {
                    System.out.println("adding : " + lookAheadToken);
                    stack.add(lookAheadToken);
                    lookAheadToken = getNextNodeToken();
                }
                currState = State.REDUCE;
            }
            if(currState == State.REDUCE) {
                currState = reduceStateFunction(stack, lookAheadToken);
            }
            if(stack.get(0).phrase == FINISHED_COMPILATION) {
                currState = State.ACCEPT;
            }

        }
        return stack.get(0);
    }
    private static State reduceStateFunction(ArrayList<NodeToken> stack, NodeToken lookAheadToken) {
        System.out.println("in reduce state function");
        System.out.println("look ahead token is : " + lookAheadToken);
        System.out.println("stack is : " + stack);
        ArrayList<Phrase> longestMatchingPhrase;
        Phrase phraseToTurnInto = null;
        ArrayList<Phrase> shiftLongPhrase = null;
        int breakFlag = 0;
        if(lookAheadToken != END_OF_FILE_TOKEN) {
            for (Pair<Phrase, ArrayList<ArrayList<Phrase>>> phraseCombo : phraseCombos) {
                if(breakFlag == 1) {
                    break;
                }
                for(ArrayList<Phrase> phraseGroup : phraseCombo.second) {
                    if(matchesPhraseWithToken(stack, phraseGroup, lookAheadToken)) {
                        System.out.println("need shift to match : " + phraseGroup);
                        shiftLongPhrase = phraseGroup;
                        phraseToTurnInto = phraseCombo.first;
                        breakFlag = 1;
                        break;
                        //return State.SHIFT;
                    } else {
                        System.out.println("    for shifting didn't match : " + phraseGroup);
                    }
                }
            }
        }
        longestMatchingPhrase = shiftLongPhrase;
        for (Pair<Phrase, ArrayList<ArrayList<Phrase>>> phraseCombo : phraseCombos) {
            for(ArrayList<Phrase> phraseGroup : phraseCombo.second) {
                if(matchesPhraseWithoutToken(stack, phraseGroup)) {
                    System.out.println("reducing matched for : " + phraseGroup);
                    if(longestMatchingPhrase == null) {
                        longestMatchingPhrase = phraseGroup;
                        phraseToTurnInto = phraseCombo.first;
                    }
                    else {
                        if(phraseLen(phraseGroup) > phraseLen(longestMatchingPhrase)) {
                            System.out.println("the phrase : " + phraseGroup + " is greater than : " + longestMatchingPhrase);
                            longestMatchingPhrase = phraseGroup;
                            phraseToTurnInto = phraseCombo.first;
                        }
                    }
                } else {
                    System.out.println("       for reducing didn't match : " + phraseGroup);
                }
            }
        }
        if(shiftLongPhrase == longestMatchingPhrase && shiftLongPhrase != null) {
            return State.SHIFT;
        }
        if(longestMatchingPhrase != null) {
            //Do Reduce
            System.out.println("tryign to replace : " + stack + ", with " + phraseToTurnInto + " from : " + longestMatchingPhrase);
            replaceNodesWithNode(stack, longestMatchingPhrase.size(), phraseToTurnInto);
            return State.REDUCE;
        }
        if(lookAheadToken == END_OF_FILE_TOKEN) {
            return State.ERROR;
        }
        for (Pair<Phrase, ArrayList<ArrayList<Phrase>>> phraseCombo : phraseCombos) {
            for(ArrayList<Phrase> phraseGroup : phraseCombo.second) {
                if(phraseGroup.size() < stack.size()+1) {
                    return State.SHIFT;
                }
            }
        }

        return State.ERROR;
    }

    private static boolean matchesPhraseWithoutToken(ArrayList<NodeToken> stack, ArrayList<Phrase> phraseGroup) {
        int actualLength = phraseGroup.size();
        int stackLength = stack.size();
        if(stackLength < actualLength) {
            return false;
        }
        for(int i = 0; i < actualLength; i++) {
            if(!stack.get(stackLength-actualLength+i).phrase.equals(phraseGroup.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchesPhraseWithToken(ArrayList<NodeToken> stack, ArrayList<Phrase> phraseGroup, NodeToken lookAheadToken) {
        int actualLength = phraseGroup.size();
        int stackLength = stack.size();
        for(int i = 1; i < phraseGroup.size(); i++) {
            if(lookAheadToken.phrase.equals(phraseGroup.get(i))) {
                if(stackLength < i) {
                    continue;
                }
                boolean continueFlag = false;
                for(int i2 = 0; i2 < i; i2++) {
                    if(!stack.get(i2+stackLength-(i)).phrase.equals(phraseGroup.get(i2))) {
                        continueFlag = true;
                        break;
                    }
                }
                if(continueFlag) {
                    continue;
                }
                return true;
            }
        }
        return false;
//        if(stackLength+1 < actualLength) {
//            return false;
//        }
//        for(int i = 0; i < actualLength-1; i++) {
//            if(!stack.get(i+stackLength-(actualLength-1)).phrase.equals(phraseGroup.get(i))) {
//                return false;
//            }
//        }
//
//        return phraseGroup.get(actualLength - 1).equals(lookAheadToken.phrase);
    }

    private static void replaceNodesWithNode(ArrayList<NodeToken> stack, int size, Phrase phraseToTurnInto) {
        NodeToken token = new NodeToken(phraseToTurnInto);
        System.out.println("it is : " + (stack.size() - (size)));
        int comparison = stack.size() - size;
        for(int i = stack.size() - 1; i >= comparison; i--) {
            System.out.println(i);
            token.childTokens.add(stack.get(i));
            stack.remove(i);
        }
        stack.add(token);
    }

    static int phraseLen(ArrayList<Phrase> phrases) {
        if(phrases.get(0) == NOTHING) {
            return 0;
        }
        return phrases.size();
    }
}
