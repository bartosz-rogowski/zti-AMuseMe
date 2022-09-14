export default function token2JSON(token){
    return JSON.parse(
        Buffer.from(token.split('.')[1], 'base64').toString()
      );
}