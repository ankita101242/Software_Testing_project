module.exports = {
  transform: {
    "^.+\\.(js|jsx|ts|tsx)$": "babel-jest",  // Babel transform for JavaScript/TypeScript files
  },
  moduleFileExtensions: ["js", "jsx", "json", "node"],  // Extensions Jest will look for
  testEnvironment: "jsdom",  // Use jsdom environment for DOM-related tests
  setupFilesAfterEnv: ["<rootDir>/setupTests.js"],  // Reference to the setupTests.js file
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy",  // Mock CSS/LESS/SCSS/SASS imports
    "\\.(png|jpg|jpeg|gif|svg)$": "/home/ankitaagrawal12/SPE/SPE_Major_Project/FRONTEND/src/__mocks__/fileMock.js"  // Mock image imports
  },
  transformIgnorePatterns: [
    "node_modules/(?!(axios)/)"  // Ensure axios and other necessary libraries are transformed
  ],
};
