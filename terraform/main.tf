resource "aws_vpc" "url_shortener_vpc" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "iac-url-shortener-vpc"
  }
}

resource "aws_subnet" "url_shortener_subnet" {
  vpc_id     = aws_vpc.url_shortener_vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "iac-url-shortener-subnet"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = aws_vpc.url_shortener_vpc.id

  tags = {
    Name = "iac-url-shortener-gateway"
  }
}

resource "aws_route_table" "url_shortener_route_table" {
  vpc_id = aws_vpc.url_shortener_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.gw.id
  }

  tags = {
    Name = "iac-url-shortener-route-table"
  }
}

resource "aws_route_table_association" "a" {
  subnet_id      = aws_subnet.url_shortener_subnet.id
  route_table_id = aws_route_table.url_shortener_route_table.id
}

resource "aws_key_pair" "deployer" {
  key_name   = "deployer-key"
  public_key = file("~/.ssh/id_rsa.pub") # Ensure you have your public key at this path
}

resource "aws_security_group" "url_shortener_ssh_sg" {
  name        = "iac-url-shortener-sg"
  description = "Allow SSH access and all outbound traffic"
  vpc_id = aws_vpc.url_shortener_vpc.id

  ingress {
    from_port = 2
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Allow SSH from anywhere
    ipv6_cidr_blocks = ["::/0"] # Allow SSH from anywhere for IPv6
  }

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1" # All protocols
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

resource "aws_instance" "url_shortener_ec2" {
  ami           = "ami-0a7d80731ae1b2435" # Ubuntu 22.04 LTS
  instance_type = "t2.micro"
  subnet_id = aws_subnet.url_shortener_subnet.id
  associate_public_ip_address = true
  key_name = aws_key_pair.deployer.key_name
  vpc_security_group_ids = [aws_security_group.url_shortener_ssh_sg.id]

  tags = {
    Name = "iac-url-shortener-api"
  }
}

output "instance_public_ip" {
  value = aws_instance.url_shortener_ec2.public_ip
}