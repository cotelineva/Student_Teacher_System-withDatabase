select * from Person
join Student on Person.idP = Student.idP;

select * from Person
join Teacher on Person.idP = Teacher.idP;

select * from Teacher
join Person on Person.idP = Teacher.idP
join Course on Teacher.idT = Course.idT;
select * from Enrolled;

select * from Person;
select * from Student;
select * from Teacher;
select * from Course;
select * from Enrolled;

delete from Person where idP = 21;
delete from Student where idP = 22;
delete from Teacher where idT = 1;
delete from Course where idC >= 5;
delete from Enrolled where idC > 0;

insert into Student(studentId,totalCredits,idP) values(2,29,20);
insert into Person(firstName,lastName) values('RADU','MAN'),('DANA','ALB');
insert into Teacher(idP) values(25),(24);
insert into Course(name,maxEnrollment,credits,idT) values('BD',10,5,3),('MAP',8,6,4);
insert into Enrolled(studentId,idC) values(5,3),(1,4),(6,4);


select Course.idC, count(*) as enrolled
from Enrolled
join Course on Course.idC = Enrolled.idC
group by Course.idC;

update Course set credits = 5 where idC = 3;
update Student set totalCredits = totalCredits - 10 where studentId > 0;

select *
from Enrolled
where idC = 4;