package rotn.nightscript.parser;

public enum Phrase {
    left_curly_bracket,
    right_curly_bracket,
    identifier,
    left_paren,
    right_paren,
    comma,
    semicolon,
    colon,
    when_keyword,
    number,
    string,
    variable, //::$somealphanumeric thing
    left_square_bracket,
    right_square_bracket,


    EVENT_ARGS_LIST, //::= left_square_bracket right_square_bracket | left_square_bracket EVENT_ARGS right_square_bracket
    EVENT_ARGS, //::= EVENT_ARG comma EVENT_ARG | EVENT_ARG
    EVENT_ARG, //::= identifier variable

    FINISHED_COMPILATION, //::= MULTI_EVENT
    MULTI_EVENT, //::= EVENT comma MULTI_EVENT | EVENT


    EVENT, //::= when_keyword identifier EVENT_ARGS_LIST left_curly_bracket MULTI_FUNCTION right_curly_bracket

    MULTI_FUNCTION, //::= FUNCTION semicolon | MULTI_FUNCTION MULTIFUNCTION


    FUNCTION, //::= identifier left_paren ARGS right_paren | identifer left_paren right_paren

    ARGS, //::= ARGS comma ARGS | FUNCTION_ARG
    FUNCTION_ARG, //::= FUNCTION | number | string | variable

    NOTHING,
    END_OF_FILE,

}
