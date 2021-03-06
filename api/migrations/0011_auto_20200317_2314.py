# Generated by Django 3.0.4 on 2020-03-17 17:44

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0010_auto_20200314_2122'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='id',
            field=models.UUIDField(default=uuid.UUID('eff50510-6876-11ea-9193-54bf644e2c92'), primary_key=True, serialize=False),
        ),
        migrations.AlterField(
            model_name='user',
            name='phone',
            field=models.CharField(default='', max_length=20),
        ),
    ]
