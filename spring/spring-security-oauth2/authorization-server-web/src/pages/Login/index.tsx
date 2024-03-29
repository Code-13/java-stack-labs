import Footer from '@/components/Footer';
// import { login } from '@/services/ant-design-pro/api';
// import { getFakeCaptcha } from '@/services/ant-design-pro/login';
import {
  AlipayOutlined,
  LockOutlined,
  MobileOutlined,
  TaobaoOutlined,
  UserOutlined,
  WeiboOutlined,
  WechatFilled,
  GithubOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormCaptcha,
  ProFormCheckbox,
  ProFormText,
} from '@ant-design/pro-components';
import { useEmotionCss } from '@ant-design/use-emotion-css';
import { FormattedMessage, SelectLang, useIntl, Helmet, useSearchParams } from '@umijs/max';
import { Alert, message, Tabs } from 'antd';
import Settings from '../../../config/defaultSettings';
import React, { useCallback, useEffect, useState } from 'react';
import { LoginResult } from '@/services/typings';
import { getCaptcha } from '@/services/login';
// import { flushSync } from 'react-dom';

const ActionIcons = () => {
  const langClassName = useEmotionCss(({ token }) => {
    return {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    };
  });

  const unSupport = useCallback(() => {
    message.warning('暂未支持');
  }, []);

  return (
    <>
      <AlipayOutlined key="AlipayOutlined" className={langClassName} onClick={unSupport} />
      <TaobaoOutlined key="TaobaoOutlined" className={langClassName} onClick={unSupport} />
      <WeiboOutlined key="WeiboOutlined" className={langClassName} onClick={unSupport} />
      <WechatFilled key="WechatOutlined" className={langClassName} onClick={unSupport} />
      <a key="githubOutlined" href="/oauth2/authorization/github-idp">
        <GithubOutlined className={langClassName} />
      </a>
    </>
  );
};

const Lang = () => {
  const langClassName = useEmotionCss(({ token }) => {
    return {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    };
  });

  return (
    <div className={langClassName} data-lang>
      {SelectLang && <SelectLang />}
    </div>
  );
};

const LoginMessage: React.FC<{
  content: string;
}> = ({ content }) => {
  return (
    <Alert
      style={{
        marginBottom: 24,
      }}
      message={content}
      type="error"
      showIcon
    />
  );
};

const Login: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [userLoginState] = useState<LoginResult>({});
  // const [type, setType] = useState<string>('account');
  const [loginType, setLoginType] = useState<string>(searchParams.get('type') || 'account');
  // const { initialState, setInitialState } = useModel('@@initialState');

  const containerClassName = useEmotionCss(() => {
    return {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    };
  });

  const intl = useIntl();

  (() => {
    // @ts-ignore
    const excMsg = window['excMsg'];
    if (excMsg) {
      message.error('Bad credentials' === excMsg ? '账号或密码错误！' : excMsg);
      // @ts-ignore
      window.excMsg = undefined;
    }
  })();

  const handleSubmit = async (values: any) => {
    const loginForm = document.createElement('form');
    try {
      loginForm.method = 'POST';
      loginForm.style.display = 'none';

      for (const key in values) {
        if (values.hasOwnProperty(key)) {
          const input = document.createElement('input');
          input.name = key;
          input.value = values[key];
          loginForm.appendChild(input);
        }
      }

      if (loginType === 'account') {
        // 账号登陆
        loginForm.action = '/login/account';
      } else if (loginType === 'phone') {
        // 手机验证码登陆
        loginForm.action = '/login/phone';
      }
      console.log(loginForm);
      document.body.appendChild(loginForm);
      loginForm.submit();
    } catch (error) {
      const defaultLoginFailureMessage = intl.formatMessage({
        id: 'pages.login.failure',
        defaultMessage: '登录失败，请重试！',
      });
      console.log(error);
      message.error(defaultLoginFailureMessage);
    } finally {
      document.body.removeChild(loginForm);
    }
  };
  const { status } = userLoginState;

  return (
    <div className={containerClassName}>
      <Helmet>
        <title>
          {intl.formatMessage({
            id: 'menu.login',
            defaultMessage: '登录页',
          })}
          - {Settings.title}
        </title>
      </Helmet>
      <Lang />
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          // logo={<img alt="logo" src="/logo.svg" />}
          title="Authorization Server"
          subTitle={intl.formatMessage({ id: 'pages.layouts.userLayout.title' })}
          initialValues={{
            autoLogin: true,
          }}
          actions={[
            <FormattedMessage
              key="loginWith"
              id="pages.login.loginWith"
              defaultMessage="其他登录方式"
            />,
            <ActionIcons key="icons" />,
          ]}
          onFinish={async (values) => {
            await handleSubmit(values);
          }}
        >
          <Tabs
            activeKey={loginType}
            onChange={(key) => {
              setSearchParams({
                type: key,
              });
              setLoginType(key);
            }}
            centered
            items={[
              {
                key: 'account',
                label: intl.formatMessage({
                  id: 'pages.login.accountLogin.tab',
                  defaultMessage: '账户密码登录',
                }),
              },
              {
                key: 'phone',
                label: intl.formatMessage({
                  id: 'pages.login.phoneLogin.tab',
                  defaultMessage: '手机号登录',
                }),
              },
            ]}
          />

          {status === 'error' && loginType === 'account' && (
            <LoginMessage
              content={intl.formatMessage({
                id: 'pages.login.accountLogin.errorMessage',
                defaultMessage: '账户或密码错误(admin/ant.design)',
              })}
            />
          )}
          {loginType === 'account' && (
            <>
              <ProFormText
                name="username"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.username.placeholder',
                  defaultMessage: '用户名: admin or user',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.username.required"
                        defaultMessage="请输入用户名!"
                      />
                    ),
                  },
                ]}
              />
              <ProFormText.Password
                name="password"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.password.placeholder',
                  defaultMessage: '密码: ant.design',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.password.required"
                        defaultMessage="请输入密码！"
                      />
                    ),
                  },
                ]}
              />
            </>
          )}

          {status === 'error' && loginType === 'phone' && <LoginMessage content="验证码错误" />}
          {loginType === 'phone' && (
            <>
              <ProFormText
                fieldProps={{
                  size: 'large',
                  prefix: <MobileOutlined />,
                }}
                name="phone"
                placeholder={intl.formatMessage({
                  id: 'pages.login.phoneNumber.placeholder',
                  defaultMessage: '手机号',
                })}
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.phoneNumber.required"
                        defaultMessage="请输入手机号！"
                      />
                    ),
                  },
                  {
                    pattern: /^1\d{10}$/,
                    message: (
                      <FormattedMessage
                        id="pages.login.phoneNumber.invalid"
                        defaultMessage="手机号格式错误！"
                      />
                    ),
                  },
                ]}
              />
              <ProFormCaptcha
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                captchaProps={{
                  size: 'large',
                }}
                placeholder={intl.formatMessage({
                  id: 'pages.login.captcha.placeholder',
                  defaultMessage: '请输入验证码',
                })}
                captchaTextRender={(timing, count) => {
                  if (timing) {
                    return `${count} ${intl.formatMessage({
                      id: 'pages.getCaptchaSecondText',
                      defaultMessage: '获取验证码',
                    })}`;
                  }
                  return intl.formatMessage({
                    id: 'pages.login.phoneLogin.getVerificationCode',
                    defaultMessage: '获取验证码',
                  });
                }}
                phoneName="phone"
                name="captcha"
                rules={[
                  {
                    required: true,
                    message: (
                      <FormattedMessage
                        id="pages.login.captcha.required"
                        defaultMessage="请输入验证码！"
                      />
                    ),
                  },
                ]}
                onGetCaptcha={async (phone) => {
                  try {
                    await getCaptcha({ phone });
                    message.success('验证码已通过短信发送至您的手机，请查收！');
                  } catch (error) {
                    message.error('获取验证码错误！');
                    throw new Error('获取验证码错误');
                  }
                }}
              />
            </>
          )}
          <div
            style={{
              marginBottom: 24,
            }}
          >
            <ProFormCheckbox noStyle name="autoLogin">
              <FormattedMessage id="pages.login.rememberMe" defaultMessage="自动登录" />
            </ProFormCheckbox>
            <span
              style={{
                float: 'right',
              }}
            >
              <a href="/register">
                <FormattedMessage id="pages.login.registerAccount" defaultMessage="注册账户" />
              </a>
              <span> / </span>
              <a href="/forget">
                <FormattedMessage id="pages.login.forgotPassword" defaultMessage="忘记密码" />
              </a>
            </span>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
