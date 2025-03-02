import { Button, Container, Group, Space, Text, Title } from "@mantine/core";
import classes from "./not-found.module.css";
import NotFoundIllustration from "./not-found-svg-illustration.tsx";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

function NotFound() {
  const navigate = useNavigate();
  const { t } = useTranslation();

  function handleOnClick() {
    navigate("/");
  }

  return (
    <Container className={classes.root}>
      <div className={classes.inner}>
        <NotFoundIllustration
          className={classes.image}
          name="404 SVG illustration"
        />
        <div className={classes.content}>
          <Title className={classes.title}>{t("error.404.title")}</Title>
          <Space h="xs" />
          <Text
            c="dimmed"
            size="lg"
            ta="center"
            className={classes.description}
          >
            {t("error.404.message")}
          </Text>
          <Space h="xl" />
          <Group justify="center">
            <Button size="md" onClick={handleOnClick}>
              {t("error.button.back")}
            </Button>
          </Group>
        </div>
      </div>
    </Container>
  );
}

export default NotFound;
