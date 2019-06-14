####Steps to deploy using AWS CLI:
1) Run the script using command `bash csye6225-aws-networking-setup.sh`
2) Script will perform following operations:
•	Create a VPC by taking VPC NAME as parameter
•	Create 3 subnets, each in different availability zone in the same region under same VPC
•	Create Internet Gateway resource
•	Attach the Internet Gateway to the created VPC
•	Create a public Route Table. Attach all subnets created above to the route table
•	Create a public route in the public route table created above with destination CIDR block `0.0.0.0/0` and internet gateway created above as the target
3) Appropriate logs are displayed

####Steps to teardown the infrastructure deployed using CLI: 
1) Run the script using command bash-aws-networking-teardown.sh
2) Appropriate logs are displayed
