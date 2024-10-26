from django.shortcuts import redirect
from allauth.account.utils import send_email_confirmation

def confirmation_required_redirect(self, request):
    # 유저에게 인증 이메일을 보냄
    send_email_confirmation(request, request.user)

    # 이메일 인증이 필요한 페이지로 리다이렉트
    return redirect("account_email_confirmation_required")

