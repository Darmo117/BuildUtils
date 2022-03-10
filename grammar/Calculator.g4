// Only direct left recursion is allowed: https://stackoverflow.com/a/41183746/3779986
grammar Calculator;

WS    : [ \n\t]+ -> skip;

LPAREN: '(';
RPAREN: ')';
COMMA : ',';
ASSIGN: ':=';

PLUS  : '+';
MINUS : '-';
MUL   : '*';
DIV   : '/';
MOD   : '%';
POWER : '^';

EQUAL : '=';
NEQUAL: '!=';
GT    : '>';
GE    : '>=';
LT    : '<';
LE    : '<=';

NOT   : '!';
AND   : '&';
OR    : '|';

TRUE  : 'true';
FALSE : 'false';

ID    : [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER: [0-9]+'.'?[0-9]*;

start: statement EOF;

statement: exp                    # Expression
         | var=ID ASSIGN expr=exp # VariableDef
         | fname=ID LPAREN ID (COMMA ID)* RPAREN ASSIGN expr=exp # FunctionDef
         ;

exp: LPAREN expr=exp RPAREN                        # Parentheses
   | operator=(MINUS | PLUS | NOT) operand=exp     # UnaryOperator
   | left=exp operator=POWER right=exp             # Operator
   | left=exp operator=(MUL | DIV | MOD) right=exp # Operator
   | left=exp operator=(PLUS | MINUS) right=exp    # Operator
   | left=exp operator=(EQUAL | NEQUAL | GT | GE | LT | LE) right=exp # Operator
   | left=exp operator=AND right=exp               # Operator
   | left=exp operator=OR right=exp                # Operator
   | name=ID LPAREN exp (COMMA exp)* RPAREN        # Function
   | ID                                            # Variable
   | NUMBER                                        # Number
   | value=(TRUE | FALSE)                          # Boolean
;
