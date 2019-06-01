/**insert into department
*（did，dname）
*/
INSERT INTO department
VALUES (00001,'营业部'),
(00002,'财务部'),
(00003,'宣传部');

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
(00002,00002,00002,'川普',21,'男'),
(00003,00003,00003,'鸡太美',38,'男'),
(00004,00001,00002,'追风少年',33,'男'),
(00005,00002,00002,'测试',99,'男');

/**insert into ch_eck
*（eid，cyear，cmonth，cday，ctime, ate，leave，duty，noduty，rest）
*/
INSERT INTO ch_eck
VALUES (00001,2019,5,21,20190521111111,0,0,1,0,0);

/**insert into work_type
*(wid，wname，pay)
*/
INSERT INTO work_type
VALUES (00001,'工作日',50),
(00002,'周末',75),
(00003,'节日',100);

/**insert into allowance
*（eid，ayear，amonth，aday，begin_time，end_time，_hours，wid，extra，state，nowtime）
*/
INSERT INTO allowance
VALUES (00001,2019,5,21,19,21,3,00001,150,0,20190521111111);


/**insert into _usr
*（eid，usr，pass，authority）
*/
INSERT INTO _usr
VALUES (00001,'蔡徐坤','cxk',0),
(00002,'川普','cp',1),
(00003,'鸡太美','jtm',0),
(00004,'追风少年','zfsn',0),
(00005,'测试','cs',0);

INSERT INTO salary
VALUES (00001,00001,00001,00001,2,28,6000,1000,7000,2019,5,21);
