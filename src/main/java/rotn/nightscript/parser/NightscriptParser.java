package rotn.nightscript.parser;

import net.minecraftforge.fml.common.Loader;
import rotn.nightscript.EventAdder;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static rotn.nightscript.parser.LexerToken.*;
import static rotn.nightscript.parser.Parser.stringHolder;

public class NightscriptParser {
    ArrayList<LexerToken> lexerTokens = new ArrayList<>();
    public static File nightscriptFolder;
    public static File createFileWithDir(File file) {
        if (!file.exists()) file.mkdirs();
        return file;
    }

    public static String doNightscriptParsingAndSetup() {
        nightscriptFolder = new File(Loader.instance().getConfigDir().toPath().getParent().toFile(), "nightscript");
        createFileWithDir(nightscriptFolder);
        for(File file : nightscriptFolder.listFiles()) {
            File contenttweakingFile = createFileWithDir(new File(file, "contenttweaking"));
            {
                File itemFile = createFileWithDir(new File(contenttweakingFile, "items"));
                File blockFile = createFileWithDir(new File(contenttweakingFile, "blocks"));
                createFileWithDir(new File(contenttweakingFile, "sounds"));
                createFileWithDir(new File(contenttweakingFile, "particles"));

                createFileWithDir(new File(blockFile, "ores"));
                createFileWithDir(new File(blockFile, "misc"));

                createFileWithDir(new File(itemFile, "foods"));
                createFileWithDir(new File(itemFile, "toolsAndWeapons"));
                createFileWithDir(new File(itemFile, "misc"));
            }
            File eventFile = createFileWithDir(new File(file, "events"));

            for(File file1 : Objects.requireNonNull(eventFile.listFiles((file2, s) -> s.endsWith(".nightscript")))) {
                try {
                    StringHolder stringHolder = new StringHolder();
                    stringHolder.s = "none";
                    EventAdder.addNodeTokensToEvent(parseNightscriptFile(file1, stringHolder));
                    if(!stringHolder.s.equals("none")) {
                        return stringHolder.s;
                    }
                } catch (IOException e) {
                    return file1.getAbsolutePath();
                }
            }

            File resourceFile = createFileWithDir(new File(file, "resources"));
            {
                File itemFile = createFileWithDir(new File(resourceFile, "items"));
                File blockFile = createFileWithDir(new File(resourceFile, "blocks"));
                createFileWithDir(new File(resourceFile, "sounds"));
                createFileWithDir(new File(resourceFile, "particles"));

                createFileWithDir(new File(blockFile, "ores"));
                createFileWithDir(new File(blockFile, "misc"));

                createFileWithDir(new File(itemFile, "foods"));
                createFileWithDir(new File(itemFile, "toolsAndWeapons"));
                createFileWithDir(new File(itemFile, "misc"));
            }
        }
        return "none";
    }
    static StringHolder errorFile;
    static File file;
    public static NodeToken parseNightscriptFile(File file, StringHolder errorFile) throws IOException {
        NightscriptParser.errorFile = errorFile;
        NightscriptParser.file = file;
        ArrayList<NodeToken> nodeTokens = new ArrayList<>();
        File text = file;
        Scanner scnr = null;
        try {
            scnr = new Scanner(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int i = 0;
        BufferedReader br = null;
        try {
                   br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringHolder stringHolder = new StringHolder();

        while ((stringHolder.s = br.readLine()) != null) {
            while (stringHolder.s.length() != 0) {
                nodeTokens.add(getNextLexerToken(stringHolder));
                if(!errorFile.s.equals("none")) {
                    throw new IOException();
                }
                //System.out.println("saved string is : " + nodeTokens.get(nodeTokens.size() - 1));
            }
        }
        for(NodeToken nodeToken : nodeTokens) {
            System.out.println("[" + nodeToken.phrase + ":" + nodeToken.relatedString + "]");
        }
        NodeToken token = Parser.Parse(nodeTokens);
        if(token == null) {
            stringHolder.s = file.getAbsolutePath();
            throw new IOException();
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
            case ' ' : lexerToken = SPACE; break;
            case '\t' : lexerToken = SPACE; break;
            default:
        }
        if(lexerToken != null) {
            NodeToken returnToken = new NodeToken(lexerToken, ""+s.s.charAt(0));;
            s.s = s.s.substring(1);
            if(lexerToken == SPACE) {
                return getNextLexerToken(s);
            }
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
            if(s.s.charAt(length+1) == '.') {
                for(int i = length+2; Character.isDigit(s.s.charAt(i)); i++) {
                    length = i;
                    if(s.s.length() == i+1) {
                        break;
                    }
                }
                if(s.s.charAt(length+1) == 'f') {
                    NodeToken returnToken = new NodeToken(FLOAT, s.s.substring(0, length+1));
                    s.s = s.s.substring(length+2);
                    return returnToken;
                } else {
                    NodeToken returnToken = new NodeToken(DOUBLE, s.s.substring(0, length+1));
                    s.s = s.s.substring(length+1);
                    return returnToken;
                }
            }
            NodeToken returnToken = new NodeToken(INTEGER, s.s.substring(0, length+1));
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
            String subString = s.s.substring(0, length+1);
            if(subString.equals("true") || subString.equals("false")) {
                NodeToken returnToken = new NodeToken(BOOL, subString);
                s.s = s.s.substring(length+1);
                return returnToken;
            }
            NodeToken returnToken = new NodeToken(IDENTIFIER, subString);
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
        if(s.s.charAt(0) == '#') {
            int length = 0;
            for(int i = 1; Character.isLetterOrDigit(s.s.charAt(i)); i++) {
                length = i;
                if(s.s.length() == i+1) {
                    break;
                }
            }
            NodeToken returnToken = new NodeToken(EVENT_LAMBDA, s.s.substring(0, length+1));
            s.s = s.s.substring(length+1);
            return returnToken;
        }
        if(s.s.charAt(0) == '@') {
            int length = 0;
            for(int i = 1; Character.isLetterOrDigit(s.s.charAt(i)) || s.s.charAt(i) == '.'; i++) {
                length = i;
                if(s.s.length() == i+1) {
                    break;
                }
            }
            NodeToken returnToken = new NodeToken(IDENTIFIER, s.s.substring(0, length+1));
            s.s = s.s.substring(length+1);
            return returnToken;
        }
        errorFile.s = file.getAbsolutePath();
        return new NodeToken(END_OF_FILE);
    }
}
