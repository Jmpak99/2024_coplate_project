# Generated by Django 3.2.25 on 2024-10-25 05:06

import coplate.validators
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('coplate', '0003_auto_20241024_0949'),
    ]

    operations = [
        migrations.CreateModel(
            name='Review',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=30)),
                ('restaurant_name', models.CharField(max_length=20)),
                ('restaurant_link', models.URLField(validators=[coplate.validators.validate_rastaurant_link])),
                ('rating', models.IntegerField(choices=[(1, 1), (2, 2), (3, 3), (4, 4), (5, 5)])),
                ('image1', models.ImageField(upload_to='')),
                ('image2', models.ImageField(upload_to='')),
                ('image3', models.ImageField(upload_to='')),
                ('content', models.TextField()),
                ('dt_created', models.DateTimeField(auto_now_add=True)),
                ('dt_updated', models.DateTimeField(auto_now=True)),
            ],
        ),
    ]
