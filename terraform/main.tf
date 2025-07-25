resource "aws_vpc" "url_shortener_vpc" {
  cidr_block = var.vpc_cidr

  tags = {
    Name = "iac-url-shortener-vpc"
  }
}

resource "aws_subnet" "url_shortener_subnet" {
  vpc_id            = aws_vpc.url_shortener_vpc.id
  cidr_block        = var.subnet_cidr
  availability_zone = var.availability_zone

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
  key_name   = var.instance_key_pair_name
  public_key = file(var.instance_public_key_path)
}

resource "aws_security_group" "url_shortener_ssh_sg" {
  name        = "iac-url-shortener-sg"
  description = "Allow SSH access and all outbound traffic"
  vpc_id = aws_vpc.url_shortener_vpc.id

  ingress {
    from_port        = 22
    to_port          = 22
    protocol         = "tcp"
    cidr_blocks      = ["0.0.0.0/0"] # Allow SSH from anywhere
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
  ami                         = data.aws_ami.ubuntu.id
  instance_type               = var.instance_type
  subnet_id                   = aws_subnet.url_shortener_subnet.id
  associate_public_ip_address = true
  key_name                    = aws_key_pair.deployer.key_name
  vpc_security_group_ids      = [aws_security_group.url_shortener_ssh_sg.id]
  user_data = file("userdata.tpl")

  tags = {
    Name = "iac-url-shortener-api"
  }
}

resource "aws_s3_bucket" "terraform_state" {
    bucket = var.state_bucket

    lifecycle {
        prevent_destroy = true
    }
}

resource "aws_s3_bucket_versioning" "versioning_example" {
  bucket = aws_s3_bucket.terraform_state.id

  versioning_configuration {
    status = "Enabled"
  }

  depends_on = [aws_s3_bucket.terraform_state]
}