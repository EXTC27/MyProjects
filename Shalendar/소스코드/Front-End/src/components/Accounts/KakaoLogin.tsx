import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import axios from 'axios';
import { url } from '../../url';
import styled from 'styled-components';
declare const window: any;
class KakaoLogin extends Component<any> {
	loginWithKakao = () => {
		window.Kakao.init('048f1536eb1eb083312cde91cbcc3a6c')
		window.Kakao.Auth.login({
			success: (authObj: any) => {
				// console.log(authObj.access_token);
				// sessionStorage.setItem('jwt', authObj.access_token);
				window.Kakao.API.request({
					url: '/v2/user/me',
					success: async (res: any) => {
						// console.log(JSON.stringify(res));
						// sessionStorage.setItem('id', res.kakao_account.email);
						// sessionStorage.setItem('pw', '1111');
						try {
              const _id = res.kakao_account.email;
              const _pw = '1111';
							await axios({
								method: 'get',
								url: `${url}/member/isExist/${_id}`
							}).then(async (res) => {
								const resData = res.data
								if (resData.state === 'FAIL') {
                  this.onLogin(_id, _pw);
									// this.props.onLogin();
									// this.props.history.push('/mainPage');
								}
								else {
									await axios({
										method: 'get',
										url: `${url}/channel/isExist/${_id}`
									}).then((res) => {
										const resData = res.data
										if (resData.state === 'FAIL') {
                      this.onLogin(_id, _pw);
											// this.props.onLogin();
											// this.props.history.push('/mainPage');
										}
										else{
											alert("계정이 존재하지 않습니다.")
											sessionStorage.clear();
											this.props.history.push('/SignupPage')
										}
									})
								}
							})
						}
						catch (err) {
              alert(err)
              console.log(err)
            }
					},
					fail: function (error: any) {
						// alert(JSON.stringify(error));
						console.log(JSON.stringify(error));
					}
				});
			},
			fail: function (err: any) {
				alert("카카오 로그인 실패");
			}
		});
  }
  

  onLogin = async (_id: any, _pw: any) => {
    try {
      // console.log(_id, _pw)
      const res = await axios({
        method: 'post',
        url: `${url}/member/login`,
        data: {
          id: _id,
          pw: _pw
        }
      });
      console.log(JSON.stringify(res.data, null, 2))
      const resData = res.data;
      if (resData.status) {
        sessionStorage.setItem('id', _id);
        sessionStorage.setItem('jwt', resData.jwt);
        sessionStorage.setItem('socialLogin', 'kakao')
        // console.log(resData)
        if (resData.data.name === 'channel') {
          sessionStorage.setItem('isChannel', 'channel');
        } else {
          sessionStorage.setItem('isChannel', 'member');
        }
        this.props.onLogin();
        this.props.history.push('/mainPage');
      } else {
        alert('이메일 혹은 비밀번호를 확인해 주세요.');
      }
    } catch (err) {
      alert(err);
    }
  };


	render() {
		return (
			<StGgLogin>
				<img src='/images/kakao_account_login_btn_medium_narrow.png' alt="" onClick={this.loginWithKakao} />
			</StGgLogin>
		);
	}
}
const StGgLogin = styled.div`
    display: flex;
    flex-flow: column wrap;
    cursor: pointer;
    &:hover{
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19)
    }
`;
export default withRouter(KakaoLogin);