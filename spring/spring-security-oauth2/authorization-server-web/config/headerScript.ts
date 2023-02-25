
export default [
  {"content":
    'window.mvcModel = ${mvcModel};' +
    'window.userInfo = ${session?.userInfo};' +
    'window.excMsg = ${session?.SPRING_SECURITY_LAST_EXCEPTION?.message};' +
    'window.sucMsg = ${session?.SUCCESS_MESSAGE};' +
    'window.exc = ${session?.SPRING_SECURITY_LAST_EXCEPTION};'
  }
]
