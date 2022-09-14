import axios from 'axios';

// const API_URL = process.env.FRONTEND_API_URL + '/auth/';
const API_URL = 'http://localhost:8080/api/';

class AuthService {
  async login(user) {
    return await axios
      .post(API_URL + 'login', {
        email: user.email,
        password: user.password
      })
      .then(response => {
        if (response.data?.["access_token"]) {
          localStorage.setItem('user_info', JSON.stringify(response.data));
          return response.data;
        }

      })
      .catch(error => {
        if (error?.response?.status === 401 || error?.response?.status === 403) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  logout() {
    localStorage.removeItem('user_info');
  }

  signup(user) {
    if( user?.birthdate == undefined){
      user.birthdate = null
    }
    return axios.post(API_URL + 'user', {
      email: user.email,
      password: user.password,
      nickname: user.nickname,
      birthdate: user.birthdate
    });
  }
}

export default new AuthService();