import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api';
const USER_API = API_URL + '/user';
const LIST_API = API_URL + '/list';
const RATE_API = API_URL + '/rate';

class UserService {
  getUserInfo(email) {
    return axios
      .get(USER_API + '/' + email, { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  editUserInfo(user, id) {
    if (user?.email == undefined) {
      user.email = ""
    }
    if (user?.password == undefined) {
      user.password = ""
    }
    if (user?.nickname == undefined) {
      user.nickname = ""
    }
    if (user?.birthdate == undefined) {
      user.birthdate = ""
    }
    let params = new URLSearchParams(Object.entries(user)).toString()
    return axios
      .put(USER_API + '/' + id + '?' + params, user, { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  deleteUser(id) {
    return axios
      .delete(USER_API + '/' + id, { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  getForLaterList(id) {
    return axios
      .get(LIST_API + '/' + id, { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  addToForLaterList(userID, albumMBID) {
    return axios.post(
      LIST_API,
      {
        albumID: albumMBID,
        userID: userID
      },
      { headers: authHeader() }
    );
  }

  removeFromForLaterList(id) {
    return axios.delete(
      LIST_API + '/' + id,
      { headers: authHeader() }
    );
  }

  addRate(userID, albumMBID, rate) {
    return axios.post(
      RATE_API,
      {
        albumID: albumMBID,
        userID: userID,
        rate: rate
      },
      { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  getUserRates(userID) {
    return axios
      .get(RATE_API + '/' + userID, { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  editRate(id, rate) {
    return axios
      .put(RATE_API,
        {
          id: id,
          rate: rate
        },
        { headers: authHeader() }
      )
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

  removeRate(id) {
    return axios.delete(
      RATE_API + '/' + id,
      { headers: authHeader() })
      .then(response => {
        return response.data
      })
      .catch(error => {
        if (error?.response?.status === 401) {
          throw new Error("Email and/or password incorrect. Try again.")
        }
        if (error?.response?.status === 403) {
          throw new Error("No credentials given or session expired. Try again after logging in.")
        }
        if (error?.code === 'ERR_NETWORK') {
          throw new Error("An error occured while connecting to the network. Make sure you are connected.")
        }
        throw new Error(error)
      });
  }

}

export default new UserService();