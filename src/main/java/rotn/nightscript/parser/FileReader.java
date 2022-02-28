package rotn.nightscript.parser;

import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static rotn.nightscript.parser.LexerToken.*;

public class FileReader {
    ArrayList<LexerToken> lexerTokens = new ArrayList<>();

    public static NodeToken parseNightscriptFile(String fileDir) {
        String filePath = Loader.instance().getConfigDir().toString();
        ArrayList<NodeToken> nodeTokens = new ArrayList<>();
        try {
            File myObj = new File(Loader.instance().getConfigDir() + fileDir);
            if (myObj.createNewFile()) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File text = new File(Loader.instance().getConfigDir() + fileDir);
        Scanner scnr = null;
        try {
            scnr = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (scnr.hasNext()) {
            StringHolder stringHolder = new StringHolder();
            stringHolder.s = scnr.next();
            while (stringHolder.s.length() != 0) {
                nodeTokens.add(getNextLexerToken(stringHolder));
                System.out.println("saved string is : " + nodeTokens.get(nodeTokens.size() - 1));
            }
        }
        return Parser.Parse(nodeTokens);
    }
    public static NodeToken getNextLexerToken(StringHolder s) {
        LexerToken lexerToken = null;
        String relatedString = null;
        switch(s.s.charAt(0)) {
            case ';' : lexerToken = LexerToken.SEMICOLON; break;
            case '(' : lexerToken = LexerToken.LEFT_PAREN;  break;
            case ')' : lexerToken = LexerToken.RIGHT_PAREN;  break;
            case '{' : lexerToken = LEFT_CURLY_BRACKET;  break;
            case '}' : lexerToken = RIGHT_CURLY_BRACKET; break;
            case ',' : lexerToken = LexerToken.COMMA; break;
            case ':' : lexerToken = LexerToken.COLON; break;
            case '[' : lexerToken = LEFT_SQUARE_BRACKET; break;
            case ']' : lexerToken = RIGHT_SQUARE_BRACKET; break;
            default:
        }
        if(lexerToken != null) {
            NodeToken returnToken = new NodeToken(lexerToken, ""+s.s.charAt(0));;
            s.s = s.s.substring(1);
            return returnToken;
        }
        if(s.s.startsWith("When")) {
            s.s = s.s.substring(4);
            return new NodeToken(WHEN_KEYWORD, "When");
        }
        if(s.s.charAt(0) == '"') {
            int length = 0;
            if(s.s.length() == 1) {
                throw new RuntimeException();
            }
            for(int i = 1; s.s.charAt(i) != '"'; i++) {
                length = i;
                if(s.s.length() == i+1) {
                    break;
                }
            }
            NodeToken returnToken = new NodeToken(STRING, s.s.substring(0, length+2));
            s.s = s.s.substring(length+2);
            return returnToken;
        }
        if(Character.isDigit(s.s.charAt(0))) {
            int length = 0;
            for(int i = 0; Character.isDigit(s.s.charAt(i)); i++) {
                length = i;
                if(s.s.length() == i+1) {
                    break;
                }
            }
            NodeToken returnToken = new NodeToken(NUMBER, s.s.substring(0, length+1));
            s.s = s.s.substring(length+1);
            return returnToken;
        }
        if(Character.isAlphabetic(s.s.charAt(0))) {
            int length = 0;
            for(int i = 0; Character.isLetterOrDigit(s.s.charAt(i)); i++) {
                length = i;
                if(s.s.length() == i+1) {
                    break;
                }
            }
            NodeToken returnToken = new NodeToken(IDENTIFIER, s.s.substring(0, length+1));
            s.s = s.s.substring(length+1);
            return returnToken;
        }
        if(s.s.charAt(0) == '$') {
            int length = 0;
            for(int i = 1; Character.isLetterOrDigit(s.s.charAt(i)); i++) {
                length = i;
                if(s.s.length() == i+1) {
                    break;
                }
            }
            NodeToken returnToken = new NodeToken(VARIABLE, s.s.substring(0, length+1));
            s.s = s.s.substring(length+1);
            return returnToken;
        }
        System.out.println("AAAA it is : " + s.s);
        throw new RuntimeException();
    }
}
