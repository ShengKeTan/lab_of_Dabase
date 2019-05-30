/*set trigger on _usr
*when delect employee on table employee
*and then delect _usr's employee information
*which has the same eid
*/
DROP TRIGGER IF EXISTS `usr_delect_employee`;
/*CREATE TRIGGER usr_delect_employee
AFTER DELETE ON `employee`
FOR EACH ROW DELETE FROM _usr WHERE _usr.eid=old.eid;*/
/*
/*set trigger on ch_eck
*when delect employee on table employee
*and then delect ch_eck's employee information
*which has the same eid
*/
DROP TRIGGER IF EXISTS `check_delect_employee`;
/*CREATE TRIGGER check_delect_employee
AFTER DELETE ON `employee`
FOR EACH ROW DELETE FROM ch_eck WHERE ch_eck.eid=old.eid;*/

/*set trigger on allowance
*when delect employee on table employee
*and then delect allowance's employee information
*which has the same eid
*/
DROP TRIGGER IF EXISTS `allowance_delect_employee`;
/*CREATE TRIGGER allowance_delect_employee
AFTER DELETE ON `employee`
FOR EACH ROW DELETE FROM allowance WHERE allowance.eid=old.eid;*/

/*set trigger on salary
*when delect employee on table employee
*and then delect salary's employee information
*which has the same eid
*/
DROP TRIGGER IF EXISTS `salary_delect_employee`;
/*CREATE TRIGGER salary_delect_employee
AFTER DELETE ON `employee`
FOR EACH ROW DELETE FROM salary WHERE salary.eid=old.eid;*/
