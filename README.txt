Description:
Program to store DNS information into database.

The program allow user to add .txt file which contains list of DNS information (domain followed by its IP address)
and store them into database.

User can get the details of DNS added into this database,
given a domain-name, it will find and return the IP address.

The DNS added is also can be removed by supplying both the domain and its IP address

There 2 ways of storing DNS can be selected in this program:
	a. In memory version, which stores all information in the memory
	b. Block file version, which converts all information into a file, and only
	     retrive the information as needed. This can prevent a lot of memory
	     usage, but it is a little bit slower though.
	