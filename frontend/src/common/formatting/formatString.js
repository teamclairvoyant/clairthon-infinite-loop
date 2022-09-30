/**
 * it formats the string dynamically and create a complete sentence i,e
 * input : formatStr("I am a {0} developer with {1} experience", "ReactJs", "6")
 * output I am a ReactJs developer with 6 experience
 * @param str input string which needs to be formatted
 * @param params list of params to replace
 * @return complete formatted string
 */

export default function formatString(str, ...params) {
  let formattedString = str;
  params?.forEach((value, index) => {
    formattedString = formattedString.replace(new RegExp(`\\{${index}\\}`, 'gu'), String(value));
  });
  return formattedString;
}
