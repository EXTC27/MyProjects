import React, { Component } from 'react';
import GoogleLogin from 'react-google-login';
import axios from 'axios';
import { url } from '../../url'
import styled from 'styled-components';
import { withRouter } from 'react-router-dom';

class GgLogin extends Component<any> {
  responseGoogle = async (res: any) => {
    // console.log(res);
    // sessionStorage.setItem('jwt', res.accessToken);
    try {
      // debugger
      const _id = res.profileObj.email;
      const _pw = '2222';
      // sessionStorage.setItem('id', _id);
      // sessionStorage.setItem('pw', '2222');
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
          try {
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
          catch (err) {console.log(err)}
        }
      })
    }
    catch (err) {console.log(err)}
  }

  responseFail = (err: any) => {
    console.error(err);
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
        sessionStorage.setItem('socialLogin', 'google')
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
        <GoogleLogin
          clientId="44884700400-kd6hmt2cbq7a2kg4274lpqfa375ms3e0.apps.googleusercontent.com"
          buttonText="Google"
          onSuccess={this.responseGoogle}
          onFailure={this.responseFail}
        />
      </StGgLogin>
    )
  }
}
const StGgLogin = styled.div`
    display: flex;
    flex-flow: column wrap;
    &:hover{
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    }
`;
export default withRouter(GgLogin);