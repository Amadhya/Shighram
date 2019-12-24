import uuid
from django.contrib.auth.models import PermissionsMixin
from django.contrib.auth.base_user import AbstractBaseUser, BaseUserManager
from django.db import models
from django.utils.timezone import now
from django.contrib.auth.hashers import (
    check_password
)


class UserManager(BaseUserManager):
    use_in_migrations = True

    def get_by_id(self, user_id):
        return self.get(id=user_id)

    def get_by_email(self, email):
        return self.filter(email=email).first()

    def get_by_phone(self, phone):
        return self.filter(phone=phone).first()

    def _create_user(self, email, password, **extra_fields):
        """
        Creates and saves a User with the given email and password.
        """
        if not email:
            raise ValueError('The given email must be set')
        email = self.normalize_email(email)
        user = self.model(email=email, **extra_fields)
        user.set_password(password)
        user.save()
        return user

    def create_user(self, email, password=None, **extra_fields):
        extra_fields.setdefault('is_superuser', False)
        return self._create_user(email, password, **extra_fields)

    def create_superuser(self, email, password, **extra_fields):
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)

        if extra_fields.get('is_staff') is not True:
            raise ValueError('Superuser must have is_staff=True.')
        if extra_fields.get('is_superuser') is not True:
            raise ValueError('Superuser must have is_superuser=True.')

        return self._create_user(email, password, **extra_fields)

    def authenticate(self, email, password):
        user = self.get_by_email(email=email)
        if user is not None:
            if check_password(password, user.password):
                return user

        return None


class User(PermissionsMixin, AbstractBaseUser):
    id = models.UUIDField(primary_key=True, null=False, default=uuid.uuid1())
    first_name = models.CharField('first name', max_length=50)
    last_name = models.CharField('last name', max_length=50)
    phone = models.CharField(max_length=20, unique=True, default='')
    email = models.EmailField(unique=True)
    date_joined = models.DateTimeField('date joined', default=now)
    is_active = models.BooleanField('active', default=True)
    is_staff = models.BooleanField('staff status', default=False)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['phone', 'first_name', 'last_name']

    objects = UserManager()

    def get_full_name(self):
        return '%s %s' % (self.first_name, self.last_name)

    def get_email(self):
        return self.email

    def serialize(self):
        return {
            'id': self.id,
            'first_name': self.first_name,
            'last_name': self.last_name,
            'email': self.email,
            'phone': self.phone,
            'date_joined': self.date_joined,
        }




