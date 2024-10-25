from django.db import models
from django.contrib.auth.models import AbstractUser
from .validators import validate_no_special_characters, validate_rastaurant_link

# Create your models here.
class User(AbstractUser):
    nickname = models.CharField(
        max_length=15,
        unique=True,
        null=True,
        validators=[validate_no_special_characters],
        error_messages={"unique": "이미 사용중인 닉네임입니다."},
    )

    def __str__(self):
        return self.email

class Review(models.Model):
    # 리뷰 제목 (최대 30자)
    title = models.CharField(max_length=30)
    
    # 식당 이름 (최대 20자)
    restaurant_name = models.CharField(max_length=20)
    
    # 식당 링크 (validator활용, URL필드)
    restaurant_link = models.URLField(validators=[validate_rastaurant_link])

    # 식당 리뷰 점수 (1,2,3,4,5 중 하나)
    RATING_CHOICES = [
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 4),
        (5, 5),
    ]
    rating = models.IntegerField(choices=RATING_CHOICES)

    # 식당 이미지 3개 업로드, 1개 피룻, 나머지 2개 선택
    image1 = models.ImageField(upload_to='review_pics')
    image2 = models.ImageField(upload_to='review_pics', blank=True)
    image3 = models.ImageField(upload_to='review_pics', blank=True)
    
    # 리뷰 작성
    content = models.TextField()
    
    # 게시글 생성 날짜 + 시간
    dt_created = models.DateTimeField(auto_now_add=True)
    
    # 게시글 마지막 수정 날짜 + 시간
    dt_updated = models.DateTimeField(auto_now=True)

    # 게시글 작성자 (작성자 삭제 시 게시글 자동 삭제)
    author = models.ForeignKey(User, on_delete=models.CASCADE)

    def __str__(self):
        return self.title
    

    
