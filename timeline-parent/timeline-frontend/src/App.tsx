// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import { BrowserRouter, Route, Routes } from "react-router-dom";
import { createTheme, ThemeProvider, useMediaQuery } from "@mui/material";
import { useMemo } from "react";
import BaseLayout from "./core/components/layouts/BaseLayout.tsx";
import LoginLayout from "./core/components/layouts/LoginLayout.tsx";
import LoginForm from "./core/components/forms/LoginForm.tsx";
import NotFound from "./core/components/pages/NotFound.tsx";

function App() {
  const prefersDarkMode = useMediaQuery("(prefers-color-scheme: dark)");
  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode: prefersDarkMode ? "dark" : "light",
        },
      }),
    [prefersDarkMode],
  );

  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<BaseLayout />}></Route>
          <Route path="/login" element={<LoginLayout />}>
            <Route index element={<LoginForm />} />
          </Route>
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;
