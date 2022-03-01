package rotn.nightscript.parser;

import java.util.ArrayList;

public class NodeToken {
    public String relatedString = null;
    public LexerToken token;
    public Phrase phrase;
    public ArrayList<NodeToken> childTokens = new ArrayList<>();
    NodeToken(Phrase phrase) {
        this.phrase = phrase;
    }
    NodeToken(LexerToken token) {
        this.token = token;
        this.phrase = getParserEnumEquiv(token);
    }
    NodeToken(LexerToken token, String relatedString) {
        this(token);
        this.relatedString = relatedString;
    }
    public Phrase getParserEnumEquiv(LexerToken token) {
        switch (token) {
            case WHEN_KEYWORD: return Phrase.when_keyword;
            case INTEGER: return Phrase.integer;
            case LEFT_CURLY_BRACKET: return Phrase.left_curly_bracket;
            case RIGHT_CURLY_BRACKET: return Phrase.right_curly_bracket;
            case IDENTIFIER: return Phrase.identifier;
            case LEFT_PAREN: return Phrase.left_paren;
            case RIGHT_PAREN: return Phrase.right_paren;
            case COMMA: return Phrase.comma;
            case EVENT_LAMBDA: return Phrase.event_lambda;
            case SEMICOLON: return Phrase.semicolon;
            case COLON: return Phrase.colon;
            case STRING: return Phrase.string;
            case VARIABLE: return Phrase.variable;
            case FLOAT: return Phrase.float_var;
            case DOUBLE: return Phrase.double_var;
            case BOOL: return Phrase.bool;
            case LEFT_SQUARE_BRACKET: return Phrase.left_square_bracket;
            case RIGHT_SQUARE_BRACKET: return Phrase.right_square_bracket;
            case END_OF_FILE: return Phrase.END_OF_FILE;
        };
        return null;
    }
    public String toString() {
        return "Phrase : " +  phrase + "";
    }
    public static void printTree(NodeToken nodeToken, int pos) {
        String printStr = "|";
        for(int i = 0; i < pos; i++) {
            printStr += " |";
        }
        printStr += "-" + nodeToken.phrase;
        if(nodeToken.relatedString != null) {
            printStr += ", '" + nodeToken.relatedString + "'";
        }
        System.out.println(printStr);
        for(int i = 0; i < nodeToken.childTokens.size(); i++) {
            printTree(nodeToken.childTokens.get(i), pos+1);
        }
    }
}
