package rotn.nightscript.parser;

public enum LexerToken {
    LEFT_CURLY_BRACKET,
    RIGHT_CURLY_BRACKET,
    IDENTIFIER,
    LEFT_PAREN,
    RIGHT_PAREN,
    COMMA,
    SEMICOLON,
    COLON,
    WHEN_KEYWORD,
    EVENT_LAMBDA,
    INTEGER,
    FLOAT,
    DOUBLE,
    STRING,
    VARIABLE,
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,

    SPACE,
    END_OF_FILE
}
