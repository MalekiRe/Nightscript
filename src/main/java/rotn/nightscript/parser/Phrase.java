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
    integer,
    float_var,
    double_var,
    event_lambda,
    string,
    variable, //::$somealphanumeric thing
    bool,
    left_square_bracket,
    right_square_bracket,


    EVENT_ARGS_LIST, //::= left_square_bracket right_square_bracket | left_square_bracket EVENT_ARGS right_square_bracket
    EVENT_ARGS, //::= EVENT_ARG comma EVENT_ARG | EVENT_ARG
    EVENT_ARG, //::= identifier variable | event_lambda

    FINISHED_COMPILATION, //::= MULTI_EVENT
    MULTI_EVENT, //::= EVENT comma MULTI_EVENT | EVENT


    EVENT, //::= when_keyword identifier EVENT_ARGS_LIST left_curly_bracket MULTI_FUNCTION right_curly_bracket

    MULTI_FUNCTION, //::= FUNCTION semicolon | MULTI_FUNCTION MULTIFUNCTION

    FUNCTION, //::= NORMAL_FUNCTION | EVENT_LAMBDA_FUNCTION
    EVENT_LAMBDA_FUNCTION, //::= event_lambda left_paren ARGS right_paren | event_lambda left_paren right_paren
    NORMAL_FUNCTION, //::= identifier left_paren ARGS right_paren | identifer left_paren right_paren

    ARGS, //::= ARGS comma ARGS | FUNCTION_ARG
    FUNCTION_ARG, //::= FUNCTION | number | string | variable

    NOTHING,
    END_OF_FILE,

}
