// Author: anonymous-xh anonymous-xh
grammar SecureL;

@header{
package com.oracle.truffle.secureLparser;
}

start: expr EOF ;

expr: prim | array ;

prim: id=sTypeId '(' lit=literal ')';

array: arr=sArray '(' type=primType ',' INT ')';
 
sTypeId: 'sInt' | 'sDouble' | 'sBool' | 'sString';

primType: 'int' | 'double' | 'bool';

sArray: 'sArray';

literal: INT | DOUBLE | BOOLEAN | STRING;

fragment DIGIT: [0-9];

INT: DIGIT+ ;

DOUBLE: DIGIT+ '.' DIGIT+ ;

BOOLEAN: 'true' | 'false' | '0' | '1' ;

STRING: '"' .*? '"' ;

// skip all whitespace
WS : (' ' | '\r' | '\t' | '\n' | '\f')+ -> skip ;