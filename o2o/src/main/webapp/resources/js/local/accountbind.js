$(function(){
	var bindUrl='/o2o/local/bindlocalauth';
	//从地址栏的URL里获取usertype,为1则前端展示系统，其余为店家管理系统
	var usertype=getQueryString('usertype');
	$('#submit').click(function(){
		var userName=$('#username').val();
		var password=$('#psw').val();
		var verifyCodeActual=$('#j_captcha').val();
		var needVerify=false;
		if(!verifyCodeActual){
			$.toast('请输入验证码！');
			return;
		}
		$.ajax({
			url:bindUrl,
			async:false,
			cache:false,
			type:"post",
			dataType:"json",
			data:{
				userName:userName,
				password:password,
				verifyCodeActual:verifyCodeActual
			},
			success:function(data){
				if(data.success){
					$.toast('绑定成功！');
					if(usertype==1){
						window.location.href='/o2o/frontend/index';
					}
					else{
						window.location.href='/o2o/shopadmin/shoplist';
					}
				}else{
					$.toast('提交失败！'+data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
});