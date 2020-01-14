import uuid
from django.utils.timezone import now
from django.db import models
from datetime import datetime


class PaymentManager(models.Manager):
    def get_by_id(self, payment_id):
        return self.filter(id=payment_id).first()

    def get_by_razorpay_order_id(self, razorpay_order_id):
        return self.filter(razorpay_order_id=razorpay_order_id).first()

    def get_by_rfid(self, rfid):
        return self.filter(rfid=rfid).first()


class Payment(models.Model):
    id = models.UUIDField(primary_key=True, null=False, default=uuid.uuid4, unique=True)
    rfid = models.CharField(max_length=25, default='')
    amount = models.CharField(max_length=10, default='0')
    slot_number = models.CharField('slot number',max_length=5, default='')
    location = models.CharField(max_length=50, default='')
    user_id = models.CharField('user id', max_length=75, default='')
    razorpay_order_id = models.CharField('razorpay order id', max_length=75, default='')
    razorpay_payment_id = models.CharField('razorpay payment id', max_length=75, default='')
    razorpay_signature = models.CharField('razorpay signature', max_length=75, default='')
    verified = models.BooleanField(default=False)
    created_on = models.DateTimeField(default=now)
    updated_on = models.DateTimeField(default=now)

    objects = PaymentManager()

    def serialize(self):
        return {
            'order_id': self.id,
            'rfid': self.rfid,
            'amount': self.amount,
            'slot_number': self.slot_number,
            'location': self.location,
            'user_id': self.user_id,
            'razorpay_order_id': self.razorpay_order_id,
            'razorpay_payment_id': self.razorpay_payment_id,
            'razorpay_signature': self.razorpay_signature,
            'payment_verified': str(self.verified),
            'created_on': self.created_on,
            'updated_on': self.updated_on,
        }

    @classmethod
    def create(cls, **kwargs):
        payment = Payment(
            rfid=kwargs.pop('rfid'),
            slot_number=kwargs.pop('slot_number'),
            location=kwargs.pop('location'),
            amount=kwargs.pop('amount')
        )

        payment.save()
        return payment
