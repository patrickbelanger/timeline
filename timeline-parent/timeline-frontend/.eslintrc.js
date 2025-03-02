module.exports = {
  rules: {
    "no-restricted-imports": [
      "error",
      {
        paths: [
          {
            name: "@mui/joy",
            importNames: ["Link"],
            message: "Use Link from 'react-router-dom' instead."
          }
        ]
      }
    ]
  }
};