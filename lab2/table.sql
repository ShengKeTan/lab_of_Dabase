DROP TABLE IF EXISTS employee;
CREATE TABLE employee
    (eid INT,
     did INT,
     pid INT,
     ename CHAR(10) NOT NULL,
     age INT,
     sex CHAR(2),
     PRIMARY KEY (eid),
     FOREIGN KEY (did) REFERENCES department(did)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;

DROP TABLE IF EXISTS department;
CREATE TABLE department
    (did INT,
     dname CHAR(10) NOT NULL,
     PRIMARY KEY (did)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;

DROP TABLE IF EXISTS ch_eck;
CREATE TABLE ch_eck
    (cid INT,
     eid INT,
     cyear INT,
     cmonth INT,
     cday INT,
     late INT DEFAULT 0,
     leave INT DEFAULT 0,
     duty INT DEFAULT 0,
     noduty INT DEFAULT 0,
     rest INT DEFAULT 0,
     PRIMARY KEY (cid),
     FOREIGN KEY (eid) REFERENCES employee(eid)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;

DROP TABLE IF EXISTS post;
CREATE TABLE post
    (pid INT,
     pname CHAR(10) NOT NULL,
     rate INT,
     bpay DECIMAL(10,2),
     PRIMARY KEY (pid)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;

DROP TABLE IF EXISTS allowance;
CREATE TABLE allowance
    (eid INT,
     ayear INT,
     amonth INT,
     aday INT,
     _hours INT,
     _type INT,
     extra DECIMAL(10,2),
     PRIMARY KEY (eid,ayear,amonth,aday),
     FOREIGN KEY (eid) REFERENCES employee(eid)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;

DROP TABLE IF EXISTS salary;
CREATE TABLE salary
    (said INT,
     eid INT,
     ename CHAR(10),
     dname CHAR(10),
     pname CHAR(10),
     mtimes INT,
     ctimes INT,
     bpay DECIMAL(10,2),
     extra DECIMAL(10,2),
     pay DECIMAL(10,2),
     syear INT,
     smonth INT,
     sday INT,
     PRIMARY KEY (said),
     FOREIGN KEY (eid) REFERENCES employee(eid),
     FOREIGN KEY (ename) REFERENCES employee(ename),
     FOREIGN KEY (dname) REFERENCES department(dname),
     FOREIGN KEY (pname) REFERENCES post(pname)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;

DROP TABLE IF EXISTS _usr;
CREATE TABLE _usr
    (eid INT,
     usr CHAR(20),
     pass CHAR(20) NOT NULL,
     authority BOOLEAN DEFAULT 0,
     PRIMARY KEY (usr),
     FOREIGN KEY (eid) REFERENCES employee(eid)
    ) ENGINE=InnoDB DEFAUTL CHARSET=utf8;