/**insert into department
*（did，dname）
*/
INSERT INTO department
VALUES (00001,'营业部'),
(00002,'财务部');

/**insert into post
*(pid，pname，rate，bpay)
*/
INSERT INTO post
VALUES (00001,'职员',0,3000),
(00002,'部门经理',1,6000),
(00003,'总经理',2,10000);

/**insert into employee
*（eid，did，pid，ename，age，sex）
*/
INSERT INTO employee
VALUES (00001,00001,00001,'蔡徐坤',18,'男'),
(00002,00002,00002,'川普',21,'男');

/**insert into ch_eck
*（cid，eid，cyear，cmonth，cday，late，leave，duty，noduty，rest）
*/
INSERT INTO ch_eck
VALUES (00001,00001,2019,5,21,0,0,1,0,0);

/**insert into allowance
*（eid，ayear，amonth，aday，_hours，_type，extra）
*/
INSERT INTO allowance
VALUES (00001,2019,5,21,3,50,150);

/**insert into _usr
*（eid，usr，pass，authority）
*/
INSERT INTO _usr
VALUES (00001,'蔡徐坤','cxk',0);
