import uuid
from django.utils.timezone import now
from django.db import models
from datetime import datetime
from api.models.user import User


class OtpManager(models.Manager):
    def get_by_id(self, otp_id):
        return self.filter(id=otp_id).first()

    def get_by_verification(self):
        return self.filter(verified=False)
    
    def get_by_token(self,token):
        return self.filter(token=token).first()

    def get_by_user(self,user):
        return self.filter(user=user)

class Otp(models.Model):
    id = models.UUIDField(primary_key=True, null=False, default=uuid.uuid4, unique=True)
    token = models.CharField(max_length=6, null=False)
    user = models.ForeignKey(User, on_delete=models.CASCADE, db_index=True, null=False)
    verified = models.BooleanField(default=False)
    created_on = models.DateTimeField(default=now)
    updated_on = models.DateTimeField(default=now)

    objects = OtpManager()

    def serialize(self):
        return {
            'id': self.id,
            'token': self.token,
            'otp_verified': str(self.verified),
            'created_on': self.created_on,
            'updated_on': self.updated_on,
        }

    @classmethod
    def create(cls, **kwargs):
        otp = Otp(
            user=kwargs.pop('user'),
            token=kwargs.pop('token'),
        )

        otp.save()
        return otp.serialize()
