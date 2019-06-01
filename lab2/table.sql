DROP TABLE IF EXISTS department;
CREATE TABLE department
    (did INT,
     dname CHAR(10) NOT NULL,
     PRIMARY KEY (did)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS post;
CREATE TABLE post
    (pid INT,
     pname CHAR(10) NOT NULL,
     rate INT,
     bpay DECIMAL(10,2),
     PRIMARY KEY (pid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS employee;
CREATE TABLE employee
    (eid INT,
     did INT,
     pid INT,
     ename CHAR(10) NOT NULL,
     age INT,
     sex CHAR(2),
     PRIMARY KEY (eid),
     FOREIGN KEY (did) REFERENCES department(did),
     FOREIGN KEY (pid) REFERENCES post(pid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS ch_eck;
CREATE TABLE ch_eck
    (eid INT,
     cyear INT,
     cmonth INT,
     cday INT,
     ctimes TIMESTAMP,
     `late` BOOLEAN DEFAULT 0,
     `leave` BOOLEAN DEFAULT 0,
     `duty` BOOLEAN DEFAULT 0,
     `noduty` BOOLEAN DEFAULT 0,
     `rest` BOOLEAN DEFAULT 0,
     PRIMARY KEY (eid,cyear,cmonth,cday),
     FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS work_type;
CREATE TABLE work_type
    (wid INT,
     wname CHAR(10),
     pay DECIMAL(10,2),
     PRIMARY KEY (wid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS allowance;
CREATE TABLE allowance
    (eid INT,
     ayear INT,
     amonth INT,
     aday INT,
     begin_time INT,
     end_time INT,
     _hours INT,
     wid INT,
     extra DECIMAL(10,2),
     state INT,
     nowtime TIMESTAMP,
     PRIMARY KEY (eid,ayear,amonth,aday,begin_time,end_time),
     FOREIGN KEY (wid) REFERENCES work_type(wid) ON UPDATE CASCADE,
     FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS salary;
CREATE TABLE salary
    (said INT,
     eid INT,
     did INT,
     pid INT,
     mtimes INT,
     ctimes INT,
     bpay DECIMAL(10,2),
     extra DECIMAL(10,2),
     pay DECIMAL(10,2),
     syear INT,
     smonth INT,
     sday INT,
     PRIMARY KEY (said),
     FOREIGN KEY (did) REFERENCES department(did),
     FOREIGN KEY (pid) REFERENCES post(pid),
     FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS _usr;
CREATE TABLE _usr
    (eid INT,
     usr CHAR(20),
     pass CHAR(20) NOT NULL,
     authority BOOLEAN DEFAULT 0,
     PRIMARY KEY (usr),
     FOREIGN KEY (eid) REFERENCES employee(eid) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;