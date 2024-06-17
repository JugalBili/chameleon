class CreateUserDTO:
    def __init__(self, email: str, password: str):
        self.email: str = email
        self.password: str = password
